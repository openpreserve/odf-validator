package org.openpreservation.odf.pkg;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.archivers.zip.UnsupportedZipFeatureException;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParsers;
import org.openpreservation.format.zip.ZipArchiveCache;
import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.format.zip.Zips;
import org.openpreservation.odf.fmt.FormatSniffer;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.OdfFormats;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.openpreservation.odf.xml.Version;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

final class PackageParserImpl implements PackageParser {
    private static final String TO_PARSE = "toParse";
    private static final String MESS_BAD_FEATURE = "Unsupported Zip feature: %s";
    private static final String MESS_IO_EXCEPTION = "IO Exception reading stream: %s";
    private static final String[] VERSION_FILE_PATHS = { Constants.PATH_MANIFEST, Constants.NAME_SETTINGS,
            Constants.NAME_CONTENT };

    static final PackageParser getInstance() {
        return new PackageParserImpl();
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

    private static final Formats sniff(final Path toSniff) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(toSniff))) {
            return FormatSniffer.sniff(bis);
        }
    }

    private ZipArchiveCache cache;
    private Formats format = Formats.UNKNOWN;
    private String mimetype = "";
    private Manifest manifest = null;
    private Version version = Version.UNKNOWN;

    private final Map<String, OdfXmlDocument> xmlDocumentMap = new HashMap<>();

    private PackageParserImpl() {
        super();
    }

    @Override
    public OdfPackage parsePackage(final Path toParse) throws ParseException, FileNotFoundException {
        Objects.requireNonNull(toParse, String.format(Checks.NOT_NULL, TO_PARSE, "Path"));
        return this.parsePackage(toParse, toParse.getFileName().toString());
    }

    @Override
    public OdfPackage parsePackage(final InputStream toParse, final String name) throws ParseException {
        Objects.requireNonNull(toParse, String.format(Checks.NOT_NULL, TO_PARSE, "InputStream"));
        Objects.requireNonNull(name, String.format(Checks.NOT_NULL, name, "String"));
        try (BufferedInputStream bis = new BufferedInputStream(toParse)) {
            final Path temp = Files.createTempFile("odf", ".pkg");
            Files.copy(bis, temp, StandardCopyOption.REPLACE_EXISTING);
            return this.parsePackage(temp, name);
        } catch (IOException e) {
            throw new ParseException("IOException occured when reading package.", e);
        }
    }

    private final OdfPackage parsePackage(final Path toParse, final String name)
            throws ParseException, FileNotFoundException {
        Checks.existingFileCheck(toParse);
        this.initialise();
        try {
            this.format = sniff(toParse);
            this.cache = Zips.zipArchiveCacheInstance(toParse);
            checkZipEntries();
            this.version = detectVersion();
            this.mimetype = getMimeEntryValue();
        } catch (final IOException e) {
            // Simply catch the exception and return a sparsely populated OdfPackage
            return OdfPackageImpl.Builder.builder().name(name).format(this.format).build();
        }
        try {
            this.manifest = parseManifest();
            this.processZipEntries();
            return this.makePackage(name);
        } catch (ParserConfigurationException | SAXException e) {
            throw new ParseException("SAX Exception while parsing package.", e);
        } catch (IOException e) {
            throw new ParseException("IOException while parsing package.", e);
        }
    }

    private final String getMimeEntryValue() throws IOException {
        return (this.cache.getZipEntry(OdfFormats.MIMETYPE) == null) ? ""
                : new String(this.cache.getEntryInputStream(OdfFormats.MIMETYPE).readAllBytes(),
                        StandardCharsets.UTF_8);
    }

    private void checkZipEntries() throws ParseException {
        final Map<String, String> badEntries = new HashMap<>();
        for (ZipEntry entry : this.cache.getZipEntries()) {
            try {
                this.cache.getEntryInputStream(entry.getName()).close();
            } catch (UnsupportedZipFeatureException e) {
                badEntries.put(entry.getName(), String.format(MESS_BAD_FEATURE, e.getFeature().toString()));
            } catch (IOException e) {
                badEntries.put(entry.getName(), String.format(MESS_IO_EXCEPTION, e.getMessage()));
            }
        }
        if (!badEntries.isEmpty()) {
            throw new ParseException(badEntries);
        }
    }

    final Version detectVersion() throws IOException {
        Version detectedVersion = Version.UNKNOWN;
        for (final String versionPath : VERSION_FILE_PATHS) {
            try (InputStream is = this.cache.getEntryInputStream(versionPath)) {
                ParseResult result = XmlParsers.getNonValidatingParser().parse(is);
                detectedVersion = Version.fromVersion(
                        result.getRootAttributeValue(String.format("%s:version", result.getRootPrefix())));
                if (!Version.UNKNOWN.equals(detectedVersion)) {
                    return detectedVersion;
                }
            } catch (ParserConfigurationException | SAXException e) {
                throw new IOException(e);
            } catch (NoSuchElementException e) {
                // Ignore and continue
            }
        }
        return detectedVersion;
    }

    private final void processZipEntries() throws ParserConfigurationException, SAXException, IOException {
        for (final ZipEntry entry : this.cache.getZipEntries()) {
            processEntry(entry);
        }
    }

    private final void processEntry(final ZipEntry entry)
            throws ParserConfigurationException, SAXException, IOException {
        final String path = entry.getName();
        if (entry.isDirectory() || (!isOdfXml(path) && !isMetaInf(path))) {
            return;
        }
        try (InputStream is = this.cache.getEntryInputStream(path)) {
            final OdfXmlDocument xmlDoc = OdfXmlDocuments.xmlDocumentFrom(is);
            if (xmlDoc != null) {
                this.xmlDocumentMap.put(path, xmlDoc);
            }
        }
    }

    private final Manifest parseManifest() throws IOException, ParserConfigurationException, SAXException {
        final ZipEntry manifestEntry = this.cache.getZipEntry(Constants.PATH_MANIFEST);
        if (manifestEntry == null) {
            return null;
        }
        try (InputStream is = this.cache.getEntryInputStream(Constants.PATH_MANIFEST)) {
            final OdfXmlDocument xmlDoc = OdfXmlDocuments.xmlDocumentFrom(is);
            if (xmlDoc != null && xmlDoc.getParseResult().isWellFormed()) {
                    return ManifestImpl.from(this.cache.getEntryInputStream(Constants.PATH_MANIFEST));
            }
        }
        return null;
    }

    private final void initialise() {
        this.format = Formats.UNKNOWN;
        this.mimetype = null;
        this.xmlDocumentMap.clear();
        this.manifest = null;
        this.version = Version.UNKNOWN;
    }

    private final OdfPackage makePackage(final String name)
            throws ParserConfigurationException, IOException, SAXException {
        final OdfPackageImpl.Builder builder = OdfPackageImpl.Builder.builder().name(name).archive(this.cache)
                .format(this.format)
                .mimetype(mimetype)
                .version(version);
        if (this.manifest != null) {
            builder.manifest(manifest);
            for (final FileEntry docEntry : manifest.getDocumentEntries()) {
                builder.document(docEntry.getFullPath(), makeDocument(docEntry));
            }
        }

        for (final Entry<String, OdfXmlDocument> docEntry : this.xmlDocumentMap.entrySet()) {
            if (isMetaInf(docEntry.getKey())) {
                builder.metaInf(docEntry.getKey(), docEntry.getValue().getParseResult());
            }
        }

        return builder.build();
    }

    private final OdfPackageDocument makeDocument(final FileEntry docEntry)
            throws ParserConfigurationException, IOException, SAXException {
        final OdfPackageDocumentImpl.Builder builder = OdfPackageDocumentImpl.Builder.of(docEntry);
        final String keyPrefix = "/".equals(docEntry.getFullPath()) ? "" : docEntry.getFullPath();
        for (final String docName : Constants.ODF_XML) {
            final String docKey = keyPrefix + docName;
            if (this.xmlDocumentMap.containsKey(docKey)) {
                builder.xmlDocument(docName, this.xmlDocumentMap.get(docKey));
                if (Constants.NAME_META.equals(docName)
                        && this.xmlDocumentMap.get(docKey).getParseResult().isWellFormed()) {
                    builder.metadata(this.cache.getEntryInputStream(docKey));
                }
            }
        }
        return builder.build();
    }

}
