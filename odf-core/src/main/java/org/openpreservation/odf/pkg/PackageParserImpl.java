package org.openpreservation.odf.pkg;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.zip.ZipArchiveCache;
import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.format.zip.Zips;
import org.openpreservation.odf.fmt.FormatSniffer;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.OdfFormats;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

final class PackageParserImpl implements PackageParser {
    private static String toParseConst = "toParse";
    private String mimetype = "";
    private ZipArchiveCache cache;
    private final Map<String, OdfXmlDocument> xmlDocumentMap = new HashMap<>();
    private Manifest manifest = null;

    private PackageParserImpl() {
        super();
    }

    public static final PackageParser getInstance() {
        return new PackageParserImpl();
    }

    @Override
    public OdfPackage parsePackage(final Path toParse) throws IOException {
        Objects.requireNonNull(toParse, String.format(Checks.NOT_NULL, toParseConst, "Path"));
        return parsePackage(toParse, toParse.getFileName().toString());
    }

    private final Formats sniff(Path toSniff) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(toSniff))) {
            return FormatSniffer.sniff(bis);
        }
    }

    private final OdfPackage parsePackage(final Path toParse, final String name) throws IOException {
        this.mimetype = null;
        this.xmlDocumentMap.clear();
        this.manifest = null;
        Formats format = Formats.UNKNOWN;
        try {
            format = sniff(toParse);
            this.cache = Zips.zipArchiveCacheInstance(toParse);
        } catch (IOException e) {
            return OdfPackageImpl.Builder.builder().name(name).format(format).build();
        }
        try {
            for (final ZipEntry entry : this.cache.getZipEntries()) {
                processEntry(entry);
            }
            return this.makePackage(name, format);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }

    private final OdfPackage makePackage(final String name, final Formats format)
            throws ParserConfigurationException, IOException, SAXException {
        OdfPackageImpl.Builder builder = OdfPackageImpl.Builder.builder().name(name).archive(this.cache).format(format)
                .mimetype(mimetype);
        if (this.manifest != null) {
            builder.manifest(manifest);
            for (FileEntry docEntry : manifest.getDocumentEntries()) {
                builder.document(docEntry.getFullPath(), makeDocument(docEntry));
            }
        }
        for (Entry<String, OdfXmlDocument> docEntry : this.xmlDocumentMap.entrySet()) {
            if (isMetaInf(docEntry.getKey())) {
                builder.metaInf(docEntry.getKey(), docEntry.getValue().getParseResult());
            }
        }

        return builder.build();
    }

    private final OdfPackageDocument makeDocument(FileEntry docEntry)
            throws ParserConfigurationException, IOException, SAXException {
        OdfPackageDocumentImpl.Builder builder = OdfPackageDocumentImpl.Builder.of(docEntry);
        final String keyPrefix = "/".equals(docEntry.getFullPath()) ? "" : docEntry.getFullPath();
        for (final String docName : Constants.ODF_XML) {
            final String docKey = keyPrefix + docName;
            if (this.xmlDocumentMap.containsKey(docKey)) {
                builder.xmlDocument(docName, this.xmlDocumentMap.get(docKey));
                if (Constants.NAME_META.equals(docName) && this.xmlDocumentMap.get(docKey).getParseResult().isWellFormed()) {
                    builder.metadata(this.cache.getEntryInputStream(docKey));
                }
            }
        }
        return builder.build();
    }

    @Override
    public OdfPackage parsePackage(final File toParse) throws IOException {
        Objects.requireNonNull(toParse, String.format(Checks.NOT_NULL, toParseConst, "File"));
        return parsePackage(toParse.toPath(), toParse.getName());
    }

    @Override
    public OdfPackage parsePackage(final InputStream toParse, final String name) throws IOException {
        Objects.requireNonNull(toParse, String.format(Checks.NOT_NULL, toParseConst, "InputStream"));
        Objects.requireNonNull(name, String.format(Checks.NOT_NULL, name, "String"));
        try (BufferedInputStream bis = new BufferedInputStream(toParse)) {
            Path temp = Files.createTempFile("odf", ".pkg");
            Files.copy(bis, temp, StandardCopyOption.REPLACE_EXISTING);
            return this.parsePackage(temp, name);
        }
    }

    private final void processEntry(final ZipEntry entry)
            throws ParserConfigurationException, SAXException, IOException {
        final String path = entry.getName();
        if (entry.isDirectory()) {
            // No need to process directories
            return;
        }
        if (OdfFormats.MIMETYPE.equals(path)) {
            // Grab the mimetype value from the MIMETYPE file
            this.mimetype = new String(this.cache.getEntryInputStream(entry.getName()).readAllBytes(),
                    StandardCharsets.UTF_8);
            return;
        }
        if (!isOdfXml(path) && !isMetaInf(path)) {
            return;
        }
        try (InputStream is = this.cache.getEntryInputStream(path)) {
            OdfXmlDocument xmlDoc = OdfXmlDocuments.xmlDocumentFrom(is);
            if (xmlDoc != null) {
                this.xmlDocumentMap.put(path, xmlDoc);
                if (xmlDoc.getParseResult().isWellFormed()) {
                    this.parseOdfXml(path);
                }
            }
        }
    }

    static final boolean isOdfXml(final String entrypath) {
        return Constants.ODF_XML.contains(new File(entrypath).getName());
    }

    static final boolean isDsig(final String entrypath) {
        if (isMetaInf(entrypath)) {
            return entrypath.contains(Constants.SIG_TERM);
        }
        return false;
    }

    static final boolean isMetaInf(final String entrypath) {
        return entrypath.startsWith(Constants.NAME_META_INF);
    }

    private final void parseOdfXml(final String entryPath)
            throws ParserConfigurationException, SAXException, IOException {
        if (Constants.PATH_MANIFEST.equals(entryPath)) {
            this.manifest = ManifestImpl.from(this.cache.getEntryInputStream(entryPath));
        }
    }
}
