package org.openpreservation.odf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.xml.XmlChecker;
import org.openpreservation.odf.xml.XmlParseResult;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "odf-sheets", mixinStandardHelpOptions = true, version = "odf-sheets 0.1", description = "Validates Open Document Format spreadsheets.")
class OdfSheets implements Callable<Integer> {
    private static final String MIME_TEMPLATE = "application/vnd.oasis.opendocument.spreadsheet-template";
    private static final String MIME_DOCUMENT = "application/vnd.oasis.opendocument.spreadsheet";
    private static final String NAME_META_INF = "META-INF";
    private static final String NAME_MANIFEST = "manifest.xml";
    private static final String PATH_MANIFEST = NAME_META_INF + "/" + NAME_MANIFEST;
    private static final String NAME_CONTENT = "content.xml";
    private static final String TAG_DOC = "office:document";
    private static final String TAG_MANIFEST = "manifest:manifest";
    private static final MessageFactory FACTORY = Messages.getInstance();

    @Parameters(index = "0", paramLabel = "toValidate", description = "An Open Document Format spreadsheet file to validate.")
    private Path toValidateFile;

    private List<Message> messages = new ArrayList<>();

    public List<Message> getErrors() {
        List<Message> errors = new ArrayList<>();
        for (Message message : this.messages) {
            if (message.isError() || message.isFatal()) {
                errors.add(message);
            }
        }
        return Collections.unmodifiableList(errors);
    }

    public List<Message> getWarnings() {
        List<Message> warnings = new ArrayList<>();
        for (Message message : this.messages) {
            if (message.isWarning()) {
                warnings.add(message);
            }
        }
        return Collections.unmodifiableList(warnings);
    }

    @Override
    public Integer call() throws Exception {
        ConsoleFormatter.colourise(FACTORY
                .getInfo("APP-1",
                        toValidateFile.toString(), "bold"));
        if (!isFile(this.toValidateFile)) {
            return results();
        }
        if (isPackage(toValidateFile)) {
            validatePackage(toValidateFile);
        } else {
            XmlChecker checker = new XmlChecker();
            XmlParseResult result = checker.parse(toValidateFile);
            if (result.isWellFormed) {
                if (result.isRootName(TAG_DOC)) {
                    this.messages.add(FACTORY.getInfo("PKG-3", result.version));
                    if (MIMETYPES.isDocument(result.mimeType)) {
                        this.messages.add(FACTORY.getInfo("PKG-4", "Document", result.mimeType));
                    } else if (MIMETYPES.isTemplate(result.mimeType)) {
                        this.messages.add(FACTORY.getInfo("PKG-4", "Template", result.mimeType));
                    } else {
                        this.messages.add(FACTORY.getError("PKG-5", result.mimeType));
                    }
                }
                result = checker.validate(toValidateFile);
                this.messages.addAll(result.messages);
            } else {
                this.messages.addAll(result.messages);
                this.messages.add(FACTORY.getError("PKG-7", toValidateFile));
            }
        }
        return results();
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new OdfSheets()).execute(args);
        System.exit(exitCode);
    }

    private Integer results() {
        Integer retStatus = 0;
        if (this.messages.isEmpty()) {
            ConsoleFormatter.info(FACTORY.getInfo("APP-2").getMessage());
        }
        for (Message message : this.messages) {
            ConsoleFormatter.colourise(message);
            if (message.isError() || message.isFatal()) {
                retStatus = 1;
            }
        }
        if (!this.getErrors().isEmpty()) {
            ConsoleFormatter.error(String.format("NOT VALID, %d errors and %d warnings found.",
                    this.getErrors().size(), this.getWarnings().size()));
        } else {
            if (!this.getWarnings().isEmpty()) {
                ConsoleFormatter.warn(String.format("VALID, no errors and %d warnings found.",
                        this.getWarnings().size()));
            } else {
                ConsoleFormatter.info("VALID, no errors and no warnings found.");
            }
        }
        return retStatus;
    }

    private boolean isFile(Path toCheck) {
        // Check that the supplied path is an existing, regular file
        if ((toCheck == null || !Files.exists(toCheck) || !Files.isRegularFile(toCheck))) {
            this.messages.add(FACTORY.getFatal("SYS-1", toCheck));
            return false;
        }
        return true;
    }

    private boolean isPackage(Path toCheck) {
        try (ZipFile zipFile = new ZipFile(toCheck.toFile())) {
            return true;
        } catch (ZipException e) {
            this.messages.add(FACTORY.getInfo("ZIP-1", toCheck));
        } catch (IOException e) {
            this.messages.add(FACTORY.getFatal("SYS-2", toCheck, e.getMessage()));
        }
        return false;
    }

    private boolean validatePackage(Path toValidate) {
        boolean isValid = true;
        boolean manifestFound = false;
        try (ZipFile zipFile = new ZipFile(toValidate.toFile())) {
            for (ZipEntry entry : zipFile.stream().toArray(ZipEntry[]::new)) {
                if ((entry.getMethod() != ZipEntry.STORED) && (entry.getMethod() != ZipEntry.DEFLATED)) {
                    this.messages.add(FACTORY.getError("ZIP-2", entry.getName()));
                    isValid = false;
                }
                if (entry.getName().startsWith(NAME_META_INF)) {
                    if (entry.isDirectory() && !NAME_META_INF.equals(entry.getName())) {
                        this.messages.add(FACTORY.getError("PKG-1", entry.getName()));
                    } else if (entry.getName().equals(PATH_MANIFEST)) {
                        manifestFound = true;
                    }
                }
            }
            if (!manifestFound) {
                this.messages.add(FACTORY.getError("PKG-2"));
                isValid = false;
            }
        } catch (IOException e) {
            this.messages.add(FACTORY.getError("ZIP-3", toValidate.toString(), e.getMessage()));
            isValid = false;
        }
        return isValid;
    }

    private boolean validateMetInfEntry(final ZipFile packageZip, final ZipEntry metaInfEntry) throws IOException {
        boolean isValid = true;
        if (metaInfEntry.getName().equals(PATH_MANIFEST)) {
            try {
                XmlChecker checker = new XmlChecker();
                XmlParseResult result = checker.parse(packageZip.getInputStream(metaInfEntry),
                        metaInfEntry.getName());
                if (result.isWellFormed) {
                    if (!result.isRootName(TAG_MANIFEST)) {
                        this.messages.add(FACTORY.getError("XML-2", metaInfEntry.getName()));
                        isValid = false;
                    }
                }
            } catch (ParserConfigurationException | SAXNotRecognizedException | SAXNotSupportedException e) {
                this.messages.add(FACTORY.getError("SYS-4", PATH_MANIFEST, e.getMessage()));
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