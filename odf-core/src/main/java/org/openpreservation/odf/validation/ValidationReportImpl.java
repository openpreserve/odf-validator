package org.openpreservation.odf.validation;

import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.xml.Metadata;

public class ValidationReportImpl implements ValidationReport {
    public static ValidationReportImpl of(final OdfPackage pkg, final ValidationResult validationResult) {
        final Metadata metadata = (pkg.getDocument() != null) ? pkg.getDocument().getMetadata() : null;
        return new ValidationReportImpl(metadata, pkg.getManifest(), validationResult, null);
    }

    public static ValidationReportImpl of(final ValidationResult validationResult) {
        return new ValidationReportImpl(null, null, validationResult, null);
    }

    public static ValidationReportImpl of(final Metadata metadata, final Manifest manifest, final ValidationResult validationResult, final ProfileResult profileResult) {
        return new ValidationReportImpl(metadata, manifest, validationResult, profileResult);
    }

    private final Metadata metadata;
    private final Manifest manifest;
    private final ValidationResult validationResult;

    private final ProfileResult profileResult;

    private ValidationReportImpl(final Metadata metadata, final Manifest manifest, final ValidationResult validationResult, final ProfileResult profileResult) {
        this.metadata = metadata;
        this.manifest = manifest;
        this.validationResult = validationResult;
        this.profileResult = profileResult;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
        result = prime * result + ((manifest == null) ? 0 : manifest.hashCode());
        result = prime * result + ((validationResult == null) ? 0 : validationResult.hashCode());
        result = prime * result + ((profileResult == null) ? 0 : profileResult.hashCode());
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
        if (validationResult == null) {
            if (other.validationResult != null)
                return false;
        } else if (!validationResult.equals(other.validationResult))
            return false;
        if (profileResult == null) {
            if (other.profileResult != null)
                return false;
        } else if (!profileResult.equals(other.profileResult))
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
    public ValidationResult getValidationResult() {
        return validationResult;
    }

    @Override
    public ProfileResult getProfileResult() {
        return profileResult;
    }

}
