package org.openpreservation.odf.validation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.xml.XmlChecker;
import org.openpreservation.odf.xml.XmlParseResult;
import org.xml.sax.SAXException;

public class Validator {
    private static final String MIME_TEMPLATE = "application/vnd.oasis.opendocument.spreadsheet-template";
    private static final String MIME_DOCUMENT = "application/vnd.oasis.opendocument.spreadsheet";
    private static final String NAME_META_INF = "META-INF/";
    private static final String NAME_MANIFEST = "manifest.xml";
    private static final String PATH_MANIFEST = NAME_META_INF + NAME_MANIFEST;
    private static final String NAME_CONTENT = "content.xml";
    private static final String TAG_DOC = "office:document";
    private static final String TAG_MANIFEST = "manifest:manifest";
    private static final MessageFactory FACTORY = Messages.getInstance();

    public Validator() {
        super();
    }

    public ValidationReport validate(final Path toValidate)
            throws ParserConfigurationException, IOException, SAXException {
        if (toValidate == null) {
            throw new IllegalArgumentException("The supplied path is null");
        }
        if (!isFile(toValidate)) {
            throw new FileNotFoundException("Path parameter is not a file: " + toValidate);
        }
        final ValidationReport report = new ValidationReport(toValidate);
        if (isPackage(toValidate)) {
            validatePackage(toValidate, report);
        } else {
            XmlChecker checker = new XmlChecker();
            XmlParseResult result = checker.parse(toValidate);
            if (result.isWellFormed) {
                if (result.isRootName(TAG_DOC)) {
                    report.add(toValidate, FACTORY.getInfo("DOC-2", result.version));
                    if (MIMETYPES.isDocument(result.mimeType)) {
                        report.add(toValidate, FACTORY.getInfo("DOC-3", "Document", result.mimeType));
                    } else if (MIMETYPES.isTemplate(result.mimeType)) {
                        report.add(toValidate, FACTORY.getInfo("DOC-3", "Template", result.mimeType));
                    } else {
                        report.add(toValidate, FACTORY.getError("DOC-4", result.mimeType));
                    }
                }
                result = checker.validate(toValidate);
                report.add(toValidate, result.messages);
            } else {
                report.add(toValidate, result.messages);
            }
        }
        return report;
    }

    private static final boolean isFile(final Path toCheck) {
        // Check that the supplied path is an existing, regular file
        return (toCheck != null && Files.exists(toCheck) && Files.isRegularFile(toCheck));
    }

    private static final boolean isPackage(final Path toCheck) throws IOException {
        try (ZipFile zipFile = new ZipFile(toCheck.toFile())) {
            return true;
        } catch (ZipException e) {
            /**
             * No need to report this as an error as it simply means that the file is not a
             * ZIP
             */
        }
        return false;
    }

    private boolean validatePackage(final Path toValidate, final ValidationReport report)
            throws IOException {
        boolean isValid = true;
        boolean manifestFound = false;
        boolean mimetypeFound = false;

        try (FileInputStream fis = new FileInputStream(toValidate.toFile()); ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry = zis.getNextEntry();
            if (entry.getName().equals("mimetype") && !entry.isDirectory()) {
                mimetypeFound = true;

                try (Scanner s = new Scanner(zis).useDelimiter("\\A")) {
                    if (!StandardCharsets.US_ASCII.newEncoder().canEncode(s.hasNext() ? s.next() : "")) {
                        report.add(Paths.get(entry.getName()), FACTORY.getError("PKG-5"));
                        isValid = false;
                    }
                }

                if (entry.getMethod() != ZipEntry.STORED) {
                    report.add(Paths.get(entry.getName()), FACTORY.getError("PKG-6"));
                }

                if (entry.getExtra() != null) {
                    report.add(Paths.get(entry.getName()), FACTORY.getError("PKG-8"));
                }
            }
        }
        try (ZipFile zipFile = new ZipFile(toValidate.toFile())) {
            for (ZipEntry entry : zipFile.stream().toArray(ZipEntry[]::new)) {
                if ((entry.getMethod() != ZipEntry.STORED) && (entry.getMethod() != ZipEntry.DEFLATED)) {
                    report.add(Paths.get(entry.getName()), FACTORY.getError("PKG-1", entry.getName()));
                    isValid = false;
                }
                if (entry.getName().equals("mimetype") && !entry.isDirectory() && !mimetypeFound) {
                    mimetypeFound = true;
                    report.add(Paths.get(entry.getName()), FACTORY.getError("PKG-7"));
                    isValid = false;
                } else if (entry.getName().startsWith(NAME_META_INF)) {
                    if (entry.isDirectory() && !NAME_META_INF.equals(entry.getName())) {
                        report.add(Paths.get(entry.getName()), FACTORY.getError("PKG-3", entry.getName()));
                    } else if (entry.getName().equals(PATH_MANIFEST)) {
                        manifestFound = true;
                    }
                }
            }
            if (!mimetypeFound) {
                report.add(toValidate, FACTORY.getWarning("PKG-2"));
            }
            if (!manifestFound) {
                report.add(toValidate, FACTORY.getError("PKG-4"));
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean validateMetaInfEntry(final ZipFile odfPackage, final ZipEntry metaInfEntry,
            final ValidationReport report) throws IOException, ParserConfigurationException, SAXException {
        boolean isValid = true;
        if (metaInfEntry.getName().equals(PATH_MANIFEST)) {
            XmlChecker checker = new XmlChecker();
            XmlParseResult result = checker.parse(odfPackage.getInputStream(metaInfEntry),
                    Paths.get(metaInfEntry.getName()));
            if ((result.isWellFormed) && (!result.isRootName(TAG_MANIFEST))) {
                report.add(Paths.get(odfPackage.getName()), FACTORY.getError("XML-2", metaInfEntry.getName()));
                isValid = false;
            }
        } else if (metaInfEntry.getName().contains("signatures")) {
            // legal so parse as a digital signature
        }
        return isValid;
    }

    public enum MIMETYPES {
        TEMPLATE(MIME_TEMPLATE),
        DOCUMENT(MIME_DOCUMENT);

        public final String value;

        private MIMETYPES(final String value) {
            this.value = value;
        }

        public static boolean isTemplate(final String mime) {
            return mime.toLowerCase().equals(TEMPLATE.value);
        }

        public static boolean isDocument(final String mime) {
            return mime.toLowerCase().equals(DOCUMENT.value);
        }
    }
}