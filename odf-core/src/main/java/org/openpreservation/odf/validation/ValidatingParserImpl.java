package org.openpreservation.odf.validation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.ValidationResult;
import org.openpreservation.format.xml.XmlValidator;
import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.Documents;
import org.openpreservation.odf.fmt.OdfFormats;
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.xml.Namespaces;
import org.openpreservation.odf.xml.OdfSchemaFactory;
import org.openpreservation.odf.xml.Version;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

final class ValidatingParserImpl implements ValidatingParser {
    private static final String TO_VALIDATE = "toValidate";
    private static final MessageFactory FACTORY = Messages.getInstance();
    private static final OdfSchemaFactory SCHEMA_FACTORY = new OdfSchemaFactory();

    static final ValidatingParserImpl getInstance()
            throws ParserConfigurationException, SAXException {
        return new ValidatingParserImpl();
    }

    private final XmlValidator validator;

    private final PackageParser packageParser;

    private final Map<String, ValidationResult> results = new HashMap<>();

    private ValidatingParserImpl()
            throws ParserConfigurationException, SAXException {
        super();
        this.packageParser = OdfPackages.getPackageParser();
        this.validator = new XmlValidator();
    }

    @Override
    public ValidationReport validatePackage(final OdfPackage toValidate) {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, TO_VALIDATE, "OdfPackage"));
        this.results.clear();
        if (!toValidate.isWellFormedZip()) {
            final ValidationReport report = ValidationReport.of(toValidate.getName());
            report.add(toValidate.getName(), FACTORY.getError("PKG-1"));
            return report;
        }
        return validate(toValidate);
    }

    @Override
    public OdfPackage parsePackage(Path toParse) throws IOException {
        return this.packageParser.parsePackage(toParse);
    }

    @Override
    public OdfPackage parsePackage(File toParse) throws IOException {
        return this.packageParser.parsePackage(toParse);
    }

    @Override
    public OdfPackage parsePackage(final InputStream toParse, final String name) throws IOException {
        return this.packageParser.parsePackage(toParse, name);
    }

    private ValidationReport validate(final OdfPackage odfPackage) {
        final ValidationReport report = ValidationReport.of(odfPackage.getName(), Documents.openDocumentOf(odfPackage));
        report.add(OdfFormats.MIMETYPE, checkMimeEntry(odfPackage));
        if (!odfPackage.hasManifest()) {
            report.add(OdfPackages.PATH_MANIFEST, FACTORY.getError("PKG-3"));
        } else {
            report.add(OdfPackages.PATH_MANIFEST, validateManifest(odfPackage));
        }
        if (!odfPackage.hasThumbnail()) {
            report.add(OdfPackages.PATH_THUMBNAIL, FACTORY.getWarning("PKG-7"));
        }
        report.addAll(this.auditZipEntries(odfPackage));
        report.addAll(this.validateOdfXmlEntries(odfPackage));
        for (final Entry<String, ValidationResult> entry : this.results.entrySet()) {
            report.add(entry.getKey(), entry.getValue().getMessages());
        }
        return report;
    }

    private final Map<String, List<Message>> validateOdfXmlEntries(final OdfPackage odfPackage) {
        final Map<String, List<Message>> messages = new HashMap<>();
        for (final String xmlPath : odfPackage.getXmlEntryPaths()) {
            ParseResult parseResult = odfPackage.getEntryXmlParseResult(xmlPath);
            if (parseResult == null) {
                continue;
            }
            List<Message> messageList = (parseResult.isWellFormed())
                    ? validateOdfXmlDocument(odfPackage, xmlPath, parseResult)
                    : parseResult.getMessages();
            messages.put(xmlPath, messageList);
        }
        return messages;
    }

    private final List<Message> validateOdfXmlDocument(final OdfPackage odfPackage, final String xmlPath,
            final ParseResult parseResult) {
        List<Message> messageList = new ArrayList<>();
        Namespaces ns = Namespaces.fromId(parseResult.getRootNamespace().getId());
        Schema schema = (ns == null) ? null
                : SCHEMA_FACTORY.getSchema(ns,
                        getVersionFromPath(odfPackage, xmlPath));
        if (schema != null) {
            try {
                ValidationResult validationResult = this.validator.validate(parseResult,
                        odfPackage.getEntryXmlStream(xmlPath), schema);
                this.results.put(xmlPath, validationResult);
            } catch (IOException e) {
                messageList.add(FACTORY.getError("CORE-3", e.getMessage(), xmlPath));
            }
        }
        return messageList;
    }

    private Version getVersionFromPath(final OdfPackage odfPackage, final String path) {
        String version = (OdfPackages.PATH_MANIFEST.equals(path) || (OdfPackages.isDsig(path))) ? odfPackage.getManifest().getVersion()
                : (getVersionFromParseResult(odfPackage.getEntryXmlParseResult(path)));
        return (version == null) ? Version.ODF_13 : Version.fromVersion(version);
    }

    private final String getVersionFromParseResult(final ParseResult result) {
        String version = "1.3";
        if (result.isWellFormed()) {
            version = result.getRootAttributeValue(String.format("%s:%s", result.getRootPrefix(), "version"));
        }
        return version;
    }

    private final List<Message> checkMimeEntry(final OdfPackage odfPackage) {
        final List<Message> messages = new ArrayList<>();
        if (odfPackage.hasMimeEntry()) {
            messages.addAll(this.validateMimeEntry(odfPackage.getZipArchive().getZipEntry(OdfFormats.MIMETYPE),
                    odfPackage.isMimeFirst()));
            messages.add(FACTORY.getInfo("DOC-3", odfPackage.getMimeType()));
        } else {
            if (odfPackage.hasManifest() && odfPackage.getManifest().getRootMediaType() != null) {
                messages.add(FACTORY.getError("MIM-4"));
            } else {
                messages.add(FACTORY.getWarning("PKG-4"));
            }
        }
        return messages;
    }

    private final List<Message> validateMimeEntry(final ZipEntry mimeEntry, final boolean isFirst) {
        final List<Message> messages = new ArrayList<>();
        if (!isFirst) {
            messages.add(FACTORY.getError("MIM-1"));
        }
        if (!mimeEntry.isStored()) {
            messages.add(FACTORY.getError("MIM-2"));
        }
        if (mimeEntry.getExtra() != null && mimeEntry.getExtra().length > 0) {
            messages.add(FACTORY.getError("MIM-3"));
        }

        return messages;
    }

    private List<Message> validateManifest(final OdfPackage odfPackage) {
        final List<Message> messages = new ArrayList<>();
        Manifest manifest = odfPackage.getManifest();
        if (manifest == null || !odfPackage.hasManifest()) {
            messages.add(FACTORY.getError("PKG-3"));
            return messages;
        }
        if (manifest.getEntry("/") == null) {
            if (!odfPackage.hasMimeEntry()) {
                messages.add(FACTORY.getWarning("MAN-7"));
            } else {
                messages.add(FACTORY.getError("MAN-5"));
            }
        } else if (hasManifestRootMime(manifest) && (odfPackage.hasMimeEntry()
                && !manifest.getRootMediaType().equals(odfPackage.getMimeType()))) {
            messages.add(FACTORY.getError("MIM-5", manifest.getRootMediaType(), odfPackage.getMimeType()));
        }
        messages.addAll(checkManifestEntries(odfPackage));
        return messages;
    }

    private boolean hasManifestRootMime(final Manifest manifest) {
        return manifest != null && manifest.getRootMediaType() != null;
    }

    private List<Message> checkManifestEntries(final OdfPackage odfPackage) {
        final List<Message> messages = new ArrayList<>();
        for (FileEntry entry : odfPackage.getManifest().getEntries()) {
            final String entryPath = entry.getFullPath();
            ZipEntry zipEntry = odfPackage.getZipArchive().getZipEntry(entryPath);
            if ("/".equals(entryPath) || entryPath.endsWith("/")) {
                // do nothing
            } else if (!isLegitimateManifestEntry(entryPath)) {
                messages.add(getManifestError(entryPath));
            } else if (zipEntry == null) {
                messages.add(FACTORY.getError("MAN-4", entryPath));
            }
            if (entry.isEncrypted() && zipEntry != null && !zipEntry.isStored() ) {
                messages.add(FACTORY.getError("PKG-8", entryPath));
            }
        }
        return messages;
    }

    private final Map<String, List<Message>> auditZipEntries(final OdfPackage odfPackage) {
        final Map<String, List<Message>> messageMap = new HashMap<>();
        final Manifest manifest = odfPackage.getManifest();
        for (ZipEntry zipEntry : odfPackage.getZipArchive().getZipEntries()) {
            final List<Message> messages = new ArrayList<>();
            if (!isCompressionValid(zipEntry)) {
                // Entries SHALL be uncompressesed (Stored) or use deflate compression
                messages.add(FACTORY.getError("PKG-2", zipEntry.getName()));
            }
            if (zipEntry.getName().startsWith(OdfPackages.NAME_META_INF)
                    && (!zipEntry.isDirectory() && !OdfPackages.PATH_MANIFEST.equals(zipEntry.getName())
                    && !OdfPackages.isDsig(zipEntry.getName()))) {
                messages.add(FACTORY.getError("PKG-5", zipEntry.getName()));
                messageMap.put(zipEntry.getName(), messages);
            }
            if (zipEntry.isDirectory() || !isLegitimateManifestEntry(zipEntry.getName())) {
                continue;
            }
            if (manifest != null && odfPackage.getManifest().getEntry(zipEntry.getName()) == null) {
                messages.add(FACTORY.getError("MAN-1", zipEntry.getName()));
            }
            messageMap.put(zipEntry.getName(), messages);
        }
        return messageMap;
    }

    static final boolean isCompressionValid(final ZipEntry entry) {
        return entry.getMethod() == java.util.zip.ZipEntry.STORED || entry.getMethod() == java.util.zip.ZipEntry.DEFLATED;
    }

    private final boolean isLegitimateManifestEntry(final String entryPath) {
        return !(OdfFormats.MIMETYPE.equals(entryPath)
                || OdfPackages.PATH_MANIFEST.equals(entryPath)
                || entryPath.startsWith(OdfPackages.NAME_META_INF));
    }

    private final Message getManifestError(final String entryPath) {
        if (OdfFormats.MIMETYPE.equals(entryPath)) {
            return FACTORY.getError("MAN-3", entryPath);
        } else if (OdfPackages.PATH_MANIFEST.equals(entryPath)) {
            return FACTORY.getError("MAN-2", entryPath);
        } else if (entryPath.startsWith(OdfPackages.NAME_META_INF)) {
            return FACTORY.getInfo("MAN-6", entryPath);
        }
        return null;
    }
}
