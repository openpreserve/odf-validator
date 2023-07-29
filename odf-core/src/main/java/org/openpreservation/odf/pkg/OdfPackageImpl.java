package org.openpreservation.odf.pkg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.xml.XmlChecker;
import org.openpreservation.odf.xml.XmlParseResult;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

public class OdfPackageImpl implements OdfPackage {

    private boolean mimeEntryFound = false;
    private boolean manifestFound = false;
    private String mimeEntry = "";
    private final Map<String, XmlParseResult> xmlResults = new HashMap<>();

    private OdfPackageImpl(final InputStream toParse) {
        super();
        if (toParse == null) {
            throw new IllegalArgumentException("Path to validate cannot be null");
        }
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
    public boolean hasManifest() {
        return this.manifestFound;
    }

    public void validate(final InputStream toValidate) throws IOException, ParserConfigurationException, SAXException {
        // Throw a Zip Stream around the passed input stream
        try (BufferedInputStream bis = new BufferedInputStream(toValidate);
                ZipInputStream zis = new ZipInputStream(bis)) {
            ZipEntry entry;
            // Process the zip entries in order
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals(Constants.MIMETYPE) && !entry.isDirectory()) {
                    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                        Checks.copyStream(zis, bos);
                        this.mimeEntry = bos.toString(StandardCharsets.US_ASCII);
                    }
                }
                if (entry.getName().equals(Constants.PATH_MANIFEST)) {
                    this.validateMetaInfEntry(zis, entry);
                }
            }
        }
    }

    private int copyEntryToFile(final InputStream source, final File dest) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(dest);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            return Checks.copyStream(source, bos);
        }
    }

    private void validateMetaInfEntry(final InputStream is, final ZipEntry metaInfEntry)
            throws IOException, ParserConfigurationException, SAXException {
        if (metaInfEntry.getName().equals(Constants.PATH_MANIFEST)) {
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
        this.copyEntryToFile(is, tempFile.toFile());
        try (InputStream tempIs = new FileInputStream(tempFile.toFile())) {
            XmlParseResult result = checker.parse(tempIs, Paths.get(metaInfEntry.getName()));
            this.xmlResults.put(metaInfEntry.getName(), result);
        }
    }
}
