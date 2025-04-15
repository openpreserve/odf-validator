package org.openpreservation.odf.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.xml.Metadata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "validation_report")
public class ValidationReportImpl implements ValidationReport {

    private final Metadata metadata;
    private final Manifest manifest;
    private final List<ValidationResult> validationResults;
;

    public static ValidationReportImpl of(final OdfPackage pkg, final List<ValidationResult> validationResults) {
        final Metadata metadata = (pkg.getDocument() != null) ? pkg.getDocument().getMetadata() : null;
        return new ValidationReportImpl(metadata, pkg.getManifest(), validationResults);
    }

    public static ValidationReportImpl of(final List<ValidationResult> validationResults) {
        return new ValidationReportImpl(null, null, validationResults);
    }

    public static ValidationReportImpl of(final Metadata metadata, final Manifest manifest, final List<ValidationResult> validationResults) {
        return new ValidationReportImpl(metadata, manifest, validationResults);
    }

    private ValidationReportImpl(final Metadata metadata, final Manifest manifest, final List<ValidationResult> validationResults) {
        this.metadata = metadata;
        this.manifest = manifest;
        this.validationResults = Collections.unmodifiableList(validationResults);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
