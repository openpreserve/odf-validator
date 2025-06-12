package org.openpreservation.odf.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.Version;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "validation_report")
public class ValidationReportImpl implements ValidationReport {

    private final String filename;
    private final Formats detectedFormat;
    private final Version version;
    private final boolean isEncrypted;
    private final Metadata metadata;
    private final Manifest manifest;
    private final List<ValidationResult> validationResults;

    public static ValidationReportImpl of(final String filename, final OdfPackage pkg, final List<ValidationResult> validationResults) {
        return new ValidationReportImpl(filename,
                                        pkg.getDetectedFormat(),
                                        pkg.getDetectedVersion(),
                                        pkg.isEncrypted(),
                                        pkg.getDocument() != null ? pkg.getDocument().getMetadata() : null,
                                        pkg.getManifest(),
                                        validationResults);
    }

    public static ValidationReportImpl of(final String filename, final List<ValidationResult> validationResults) {
        return new ValidationReportImpl(filename, Formats.UNKNOWN, Version.UNKNOWN, false, null, null, validationResults);
    }

    public static ValidationReportImpl of(final String filename, OdfXmlDocument document, final List<ValidationResult> validationResults) {
        return new ValidationReportImpl(filename,
                                        document.getFormat(),
                                        document.getVersion(),
                                        false,
                                        null,
                                        null,
                                        validationResults);
    }

    public static ValidationReportImpl of(final String filename, OpenDocument document, final List<ValidationResult> validationResults) {
        return new ValidationReportImpl(filename,
                                        document.getFormat(),
                                        document.getVersion(),
                                        document.isPackage() ? document.getPackage().isEncrypted() : false,
                                        document.getDocument() != null ? document.getDocument().getMetadata() : null,
                                        document.isPackage() ? document.getPackage().getManifest() : null,
                                        validationResults);
    }

    public static ValidationReportImpl of(final String filename, final Metadata metadata, final Manifest manifest, final List<ValidationResult> validationResults) {
        return new ValidationReportImpl(filename, Formats.UNKNOWN, Version.UNKNOWN, false, metadata, manifest, validationResults);
    }

    private ValidationReportImpl(final String filename, final Formats detectedFormat, final Version version, final boolean isEncrypted, final Metadata metadata, final Manifest manifest, final List<ValidationResult> validationResults) {
        this.filename = filename;
        this.detectedFormat = detectedFormat;
        this.version = version;
        this.isEncrypted = isEncrypted;
        this.metadata = metadata;
        this.manifest = manifest;
        this.validationResults = Collections.unmodifiableList(validationResults);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filename == null) ? 0 : filename.hashCode());
        result = prime * result + ((detectedFormat == null) ? 0 : detectedFormat.hashCode());
        result = prime * result + ((isEncrypted) ? 1231 : 1237);
        result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
        result = prime * result + ((manifest == null) ? 0 : manifest.hashCode());
        result = prime * result + ((validationResults == null) ? 0 : validationResults.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ValidationReportImpl other = (ValidationReportImpl) obj;
        if (filename == null) {
            if (other.filename != null)
                return false;
        } else if (!filename.equals(other.filename))
            return false;
        if (detectedFormat == null) {
            if (other.detectedFormat != null)
                return false;
        } else if (!detectedFormat.equals(other.detectedFormat))
            return false;
        if (isEncrypted != other.isEncrypted)
            return false;
        if (metadata == null) {
            if (other.metadata != null)
                return false;
        } else if (!metadata.equals(other.metadata))
            return false;
        if (manifest == null) {
            if (other.manifest != null)
                return false;
        } else if (!manifest.equals(other.manifest))
            return false;
        if (validationResults == null) {
            if (other.validationResults != null)
                return false;
        } else if (!validationResults.equals(other.validationResults))
            return false;
        return true;
    }

    @Override
    public Formats getDetectedFormat() {
        return this.detectedFormat;
    }

    @Override
    public String getFormat() {
        return this.detectedFormat.mime;
    }

    @Override
    public Version getVersion() {
        return this.version;
    }

    @Override
    public String getVersionString() {
        return this.version.version;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    @Override
    public boolean isValid() {
        for (ValidationResult result : this.validationResults) {
            if (!result.isValid()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Manifest getManifest() {
        return manifest;
    }

    @Override
    public List<ValidationResult> getValidationResults() {
        return validationResults;
    }

    @Override
    public List<Check> getChecks() {
        List<Check> messages =  new ArrayList<>();
        if (this.validationResults != null) {
            for (ValidationResult result : this.validationResults) {
                messages.addAll(result.getChecks());
            }
        }
        return messages;
    }

    @Override
    public boolean hasSeverity(Severity severity) {
        for (Check check : this.getChecks()) {
            if (check.getMessage().getSeverity() == severity) {
                return true;
            }
        }
        return false;
    }
}
