package org.openpreservation.odf.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlValidationResult;
import org.openpreservation.format.xml.XmlValidator;
import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.odf.fmt.OdfFormats;
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.MessageFactory;
import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.messages.Parameter.ParameterList;
import org.openpreservation.odf.xml.ExtendedConformanceFilter;
import org.openpreservation.odf.xml.OdfNamespaces;
import org.openpreservation.odf.xml.OdfSchemaFactory;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.openpreservation.odf.xml.Version;
import org.openpreservation.utils.Checks;
import org.xml.sax.SAXException;

final class ValidatingParserImpl implements ValidatingParser {
    private static final String TO_VALIDATE = "toValidate";
    private static final MessageFactory FACTORY = Messages.getInstance();
    private static final OdfSchemaFactory SCHEMA_FACTORY = new OdfSchemaFactory();

    private final boolean isExtended;
    private final XmlValidator validator;
    private final PackageParser packageParser;
    private final Map<String, XmlValidationResult> results = new HashMap<>();

    static final ValidatingParserImpl getInstance(final boolean isExtended)
            throws ParserConfigurationException, SAXException {
        return new ValidatingParserImpl(isExtended);
    }

    private ValidatingParserImpl(final boolean isExtended)
            throws ParserConfigurationException, SAXException {
        super();
        this.isExtended = isExtended;
        this.packageParser = OdfPackages.getPackageParser();
        this.validator = new XmlValidator();
    }

    @Override
    public ValidationResult validatePackage(final OdfPackage toValidate) {
        Objects.requireNonNull(toValidate, String.format(Checks.NOT_NULL, TO_VALIDATE, "OdfPackage"));
        this.results.clear();
        if (!toValidate.isWellFormedZip()) {
            final ValidationResult result = ValidationResultImpl.of("ODF Specification Validation");
            result.getMessageLog().add(toValidate.getName(), FACTORY.getError("PKG-1"));
            return result;
        }
        return validate(toValidate);
    }

    @Override
    public OdfPackage parsePackage(Path toParse) throws ParseException, FileNotFoundException {
        return this.packageParser.parsePackage(toParse);
    }

    @Override
    public OdfPackage parsePackage(final InputStream toParse, final String name) throws ParseException {
        return this.packageParser.parsePackage(toParse, name);
    }

    private ValidationResult validate(final OdfPackage odfPackage) {
        final ValidationResult result = ValidationResultImpl.of("ODF Specification Validation");
        result.getMessageLog().add("package", FACTORY.getInfo("DOC-2", Messages.parameterListInstance().add("version", odfPackage.getDetectedVersion().version)));
        result.getMessageLog().add(OdfFormats.MIMETYPE, checkMimeEntry(odfPackage));
        result.getMessageLog().add(OdfPackages.PATH_MANIFEST, validateManifest(odfPackage));
        if (!odfPackage.hasThumbnail()) {
            result.getMessageLog().add(OdfPackages.PATH_THUMBNAIL, FACTORY.getWarning("PKG-7"));
        }
        result.getMessageLog().add(this.auditZipEntries(odfPackage));
        result.getMessageLog().add(this.validateOdfXmlEntries(odfPackage));
        for (final Entry<String, XmlValidationResult> entry : this.results.entrySet()) {
            result.getMessageLog().add(entry.getKey(), entry.getValue().getMessages());
        }
        return result;
    }

    private final Map<String, List<Message>> validateOdfXmlEntries(final OdfPackage odfPackage) {
        final Map<String, List<Message>> messages = new HashMap<>();
        for (final FileEntry xmlEntry : odfPackage.getXmlEntries()) {
            messages.put(xmlEntry.getFullPath(), validateXmlEntry(xmlEntry, odfPackage));
        }
        for (final Entry<String, ParseResult> entry : odfPackage.getMetaInfMap().entrySet()) {
            messages.put(entry.getKey(), validatePackageXml(entry.getKey(), odfPackage, entry.getValue()));
        }
        return messages;
    }

    private final List<Message> validateXmlEntry(final FileEntry xmlEntry, final OdfPackage odfPackage) {
        final String xmlEntryPath = xmlEntry.getFullPath();
        if (xmlEntryPath.equals("/")) {
            return new ArrayList<>();
        }
        if (xmlEntry.isEncrypted()) {
            return Arrays.asList(FACTORY.getWarning("PKG-10", Messages.parameterListInstance().add("entryPath", xmlEntryPath)));
        }
        return validatePackageXml(xmlEntryPath, odfPackage, odfPackage.getEntryXmlParseResult(xmlEntryPath));

    }

    private final List<Message> validatePackageXml(final String xmlPath, final OdfPackage odfPackage,
            final ParseResult parseResult) {
        if (parseResult == null) {
            return new ArrayList<>();
        }
        return (parseResult.isWellFormed())
                ? validateOdfXmlDocument(odfPackage, xmlPath, parseResult)
                : parseResult.getMessages();
    }

    private final List<Message> validateOdfXmlDocument(final OdfPackage odfPackage, final String xmlPath,
            final ParseResult parseResult) {
        List<Message> messageList = new ArrayList<>();
        OdfNamespaces ns = OdfNamespaces.fromId(parseResult.getRootNamespace().getId());
        if (OdfXmlDocuments.odfXmlDocumentOf(parseResult).isExtended()) {
            ParameterList params = Messages.parameterListInstance().add("namespaces", Utils.collectNsPrefixes(OdfXmlDocuments.odfXmlDocumentOf(parseResult)
                            .getForeignNamespaces()));
            messageList.add(FACTORY.getMessage("DOC-8", this.isExtended ? Severity.INFO : Severity.ERROR, params));
            if (!this.isExtended) {
                return messageList;
            }
        }
        Version version = getVersionFromPath(odfPackage, xmlPath);
        if (version == Version.UNKNOWN) {
            version = Version.ODF_11;
        }
        Schema schema = (ns == null) ? null
                : SCHEMA_FACTORY.getSchema(ns,
                        version);
        if (schema != null) {
            try {
                XmlValidationResult validationResult = (this.isExtended) ?
                            this.validator.validate(parseResult, odfPackage.getEntryXmlStream(xmlPath), schema, new ExtendedConformanceFilter(version)) :
                            this.validator.validate(parseResult, odfPackage.getEntryXmlStream(xmlPath), schema);
                this.results.put(xmlPath, validationResult);
            } catch (IOException e) {
                messageList.add(FACTORY.getError("CORE-3", Messages.parameterListInstance().add("message", e.getMessage()).add("xmlPath", xmlPath)));
            } catch (SAXException | ParserConfigurationException e) {
                messageList.add(FACTORY.getError("CORE-4", Messages.parameterListInstance().add("message", e.getMessage()).add("xmlPath", xmlPath)));
            }
        }
        return messageList;
    }

    private Version getVersionFromPath(final OdfPackage odfPackage, final String path) {
        String version = (OdfPackages.PATH_MANIFEST.equals(path) || (OdfPackages.isDsig(path)))
                ? odfPackage.getManifest().getVersion()
                : (getVersionFromParseResult(odfPackage.getEntryXmlParseResult(path)));
        return (version == null) ? odfPackage.getDetectedVersion() : Version.fromVersion(version);
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
            messages.add(FACTORY.getInfo("DOC-3", Messages.parameterListInstance().add("mimetype", odfPackage.getMimeType())));
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
            messages.add(FACTORY.getError("MIM-5", Messages.parameterListInstance().add("rootMimeType", manifest.getRootMediaType()).add("packageMimeType", odfPackage.getMimeType())));
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
                messages.add(FACTORY.getError("MAN-4", Messages.parameterListInstance().add("entryPath", entryPath)));
            }
            if (entry.isEncrypted() && zipEntry != null && !zipEntry.isStored()) {
                messages.add(FACTORY.getError("PKG-8", Messages.parameterListInstance().add("entryPath", entryPath)));
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
                messages.add(FACTORY.getError("PKG-2", Messages.parameterListInstance().add("entryPath", zipEntry.getName())));
                messageMap.put(zipEntry.getName(), messages);
            }
            if (zipEntry.getName().startsWith(OdfPackages.NAME_META_INF)
                    && (!zipEntry.isDirectory() && !OdfPackages.PATH_MANIFEST.equals(zipEntry.getName())
                            && !OdfPackages.isDsig(zipEntry.getName()))) {
                messages.add(FACTORY.getError("PKG-5", Messages.parameterListInstance().add("entryPath", zipEntry.getName())));
                messageMap.put(zipEntry.getName(), messages);
            }
            if (zipEntry.isDirectory() || !isLegitimateManifestEntry(zipEntry.getName())) {
                continue;
            }
            if (manifest != null && odfPackage.getManifest().getEntry(zipEntry.getName()) == null) {
                messages.add(FACTORY.getError("MAN-1", Messages.parameterListInstance().add("entryPath", zipEntry.getName())));
            }
            messageMap.put(zipEntry.getName(), messages);
        }
        return messageMap;
    }

    static final boolean isCompressionValid(final ZipEntry entry) {
        return entry.getMethod() == java.util.zip.ZipEntry.STORED
                || entry.getMethod() == java.util.zip.ZipEntry.DEFLATED;
    }

    private final boolean isLegitimateManifestEntry(final String entryPath) {
        return !(OdfFormats.MIMETYPE.equals(entryPath)
                || OdfPackages.PATH_MANIFEST.equals(entryPath)
                || entryPath.startsWith(OdfPackages.NAME_META_INF));
    }

    private final Message getManifestError(final String entryPath) {
        if (OdfFormats.MIMETYPE.equals(entryPath)) {
            return FACTORY.getError("MAN-3", Messages.parameterListInstance().add("entryPath", entryPath));
        } else if (OdfPackages.PATH_MANIFEST.equals(entryPath)) {
            return FACTORY.getError("MAN-2", Messages.parameterListInstance().add("entryPath", entryPath));
        } else if (entryPath.startsWith(OdfPackages.NAME_META_INF)) {
            return FACTORY.getInfo("MAN-6", Messages.parameterListInstance().add("entryPath", entryPath));
        }
        return null;
    }
}
