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
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.format.zip.ZipArchiveCache;
import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.format.zip.Zips;
import org.openpreservation.odf.fmt.FormatSniffer;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

final class PackageParserImpl implements PackageParser {
    private static String toParseConst = "toParse";
    private final XmlParser checker;
    private String mimetype = "";
    private ZipArchiveCache cache;
    private final Map<String, ParseResult> parseResults = new HashMap<>();
    private Manifest manifest = null;
    private Metadata metadata = null;

    private PackageParserImpl()
            throws ParserConfigurationException, SAXException {
        super();
        this.checker = new XmlParser();
    }

    public static final PackageParser getInstance()
            throws ParserConfigurationException, SAXException {
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
        this.mimetype = "";
        this.parseResults.clear();
        this.manifest = null;
        this.metadata = null;
        final Formats format = sniff(toParse);
        this.cache = Zips.zipArchiveCacheInstance(toParse);
        for (final ZipEntry entry : this.cache.getZipEntries()) {
            try {
                processEntry(entry);
            } catch (ParserConfigurationException | SAXException e) {
                throw new IOException(e);
            }
        }
        return OdfPackageImpl.Builder.builder().name(name).archive(this.cache).format(format).mimetype(mimetype)
                .parseResults(parseResults).manifest(manifest).metadata(metadata).build();
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
            throws IOException, ParserConfigurationException, SAXException {
        final String path = entry.getName();
        final String name = new File(path).getName();
        if (entry.isDirectory()) {
            // No need to process directories
            return;
        }
        if (Constants.MIMETYPE.equals(path)) {
            // Grab the mimetype value from the MIMETYPE file
            this.mimetype = new String(this.cache.getEntryInputStream(entry.getName()).readAllBytes(),
                    StandardCharsets.UTF_8);
            return;
        }
        if (isDsig(path)) {

        }
        if (!isOdfXml(path) && !isDsig(path)) {
            return;
        }
        try (InputStream is = this.cache.getEntryInputStream(path)) {
            ParseResult result = this.checker.parse(is);
            if (result != null) {
                this.parseResults.put(path, result);
                if (!result.isWellFormed()) {
                    return;
                }
            }
        }
        this.parseOdfXml(path, name);
    }

    private final boolean isOdfXml(final String entrypath) {
        final String name = new File(entrypath).getName();
        return Constants.NAME_MANIFEST.equals(name) || Constants.NAME_META.equals(name)
                || Constants.NAME_CONTENT.equals(name) || Constants.NAME_STYLES.equals(name) || Constants.NAME_MANIFEST_RDF.equals(name);
    }

    static final boolean isDsig(final String entrypath) {
        if (entrypath.startsWith(Constants.NAME_META_INF)) {
            return entrypath.contains(Constants.SIG_TERM);
        }
        return false;
    }

    private final void parseOdfXml(final String entryPath, final String entryName) throws ParserConfigurationException, SAXException, IOException {
        if (Constants.PATH_MANIFEST.equals(entryPath)) {
            this.manifest = ManifestImpl.from(this.cache.getEntryInputStream(entryPath));
        } else if (Constants.NAME_META.equals(entryName)) {
            this.metadata = MetadataImpl.from(this.cache.getEntryInputStream(entryPath));
        }
    }
}
