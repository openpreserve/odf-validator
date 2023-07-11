package org.openpreservation.odf.pkg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.xml.XmlChecker;
import org.openpreservation.odf.xml.XmlParseResult;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

public class OdfPackageImpl implements OdfPackage {
    static final String MIMETYPE = "mimetype";
    private static final String NAME_META_INF = "META-INF/";
    private static final String NAME_MANIFEST = "manifest.xml";
    private static final String PATH_MANIFEST = NAME_META_INF + NAME_MANIFEST;
    private static final String NAME_CONTENT = "content.xml";
    private static final String TAG_MANIFEST = "manifest:manifest";
    private static final MessageFactory FACTORY = Messages.getInstance();

    private boolean mimeEntryFound = false;
    private boolean manifestFound = false;
    private String mimeEntry = "";
    private final Path path;
    private final ValidationReport report;
    private final Set<Path> entryPaths = new HashSet<>();

    public OdfPackageImpl(final Path toValidate) {
        super();
        this.path = toValidate;
        report = new ValidationReport(path);
    }

    @Override
    public String getMimeEntry() {
        return this.mimeEntry;
    }

    @Override
    public boolean hasMimeEntry() {
        return this.mimeEntryFound;
    }

    @Override
    public ValidationReport validate()
            throws IOException, ParserConfigurationException, SAXException {
        try (InputStream is = new FileInputStream(this.path.toFile())) {
            validate(is);
        }
        if (!mimeEntryFound) {
            report.add(path, FACTORY.getWarning("PKG-2"));
        }
        if (!manifestFound) {
            report.add(path, FACTORY.getError("PKG-4"));
        }
        return report;
    }

    public void validate(final InputStream toValidate) throws IOException, ParserConfigurationException, SAXException {
        int entryCount = 0;
        // Throw a Zip Stream around the passed input stream
        try (BufferedInputStream bis = new BufferedInputStream(toValidate);
                ZipInputStream zis = new ZipInputStream(bis)) {
            ZipEntry entry;
            // Process the zip entries in order
            while ((entry = zis.getNextEntry()) != null) {
                entryCount++;
                if (!entry.isDirectory()) {
                    entryPaths.add(Paths.get(entry.getName()));
                }
                if ((entry.getMethod() != ZipEntry.STORED) && (entry.getMethod() != ZipEntry.DEFLATED)) {
                    // Entries SHALL be uncompressesed (Stored) or use deflate compression
                    report.add(Paths.get(entry.getName()), FACTORY.getError("PKG-1", entry.getName()));
                }
                if (entry.getName().equals(MIMETYPE) && !entry.isDirectory()) {
                    // Check the mimetype entry when it's found
                    this.validateMimeEntry(zis, entry, entryCount == 1);
                }
                if (entry.getName().startsWith(NAME_META_INF)) {
                    if (entry.isDirectory() && !NAME_META_INF.equals(entry.getName())) {
                        report.add(Paths.get(entry.getName()), FACTORY.getError("PKG-3", entry.getName()));
                    } else if (entry.getName().equals(PATH_MANIFEST)) {
                        this.validateMetaInfEntry(zis, entry);
                    }
                }
            }
        }
    }

    public Set<Path> getEntryPaths() {
        return this.entryPaths;
    }

    public InputStream getEntryStream(Path entryPath) throws IOException {
        try (ZipFile zipFile = new ZipFile(this.path.toFile())) {
            ZipEntry entry = zipFile.getEntry(entryPath.toString());
            return zipFile.getInputStream(entry);
        }
    }

    private void validateMimeEntry(final InputStream is, final ZipEntry mimeEntry, final boolean isFirst)
            throws IOException {
        mimeEntryFound = true;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Checks.copyStream(is, bos);
            this.mimeEntry = bos.toString(StandardCharsets.US_ASCII);
        }
        if (!isFirst) {
            report.add(Paths.get(mimeEntry.getName()), FACTORY.getError("PKG-7"));
        }
        if (mimeEntry.getMethod() != ZipEntry.STORED) {
            report.add(Paths.get(mimeEntry.getName()), FACTORY.getError("PKG-6"));
        }

        if (mimeEntry.getExtra() != null) {
            report.add(Paths.get(mimeEntry.getName()), FACTORY.getError("PKG-8"));
        }
    }

    private int copyEntryToFile(final InputStream source, final Path dest) throws IOException {
        return this.copyEntryToFile(source, dest.toFile());
    }

    private int copyEntryToFile(final InputStream source, final File dest) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(dest);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            return Checks.copyStream(source, bos);
        }
    }

    private void validateMetaInfEntry(final InputStream is, final ZipEntry metaInfEntry)
            throws IOException, ParserConfigurationException, SAXException {
        if (metaInfEntry.getName().equals(PATH_MANIFEST)) {
            this.manifestFound = true;
            this.validateManifest(is, metaInfEntry);
        } else if (metaInfEntry.getName().contains("signatures")) {
            // legal so parse as a digital signature
        }
    }

    private void validateManifest(final InputStream is, final ZipEntry metaInfEntry)
            throws ParserConfigurationException, IOException, SAXException {
        XmlChecker checker = new XmlChecker();
        Path tempFile = Files.createTempFile(null, null);
        this.copyEntryToFile(is, tempFile);
        try (InputStream tempIs = new FileInputStream(tempFile.toFile())) {
            XmlParseResult result = checker.parse(tempIs, Paths.get(metaInfEntry.getName()));
            if ((result.isWellFormed) && (!result.isRootName(TAG_MANIFEST))) {
                report.add(Paths.get(metaInfEntry.getName()), FACTORY.getError("XML-2", metaInfEntry.getName()));
            }
        }
    }
}
