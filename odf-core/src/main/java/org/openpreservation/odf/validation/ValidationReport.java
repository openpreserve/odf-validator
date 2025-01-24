package org.openpreservation.odf.validation;

import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.xml.Metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ValidationReportImpl.class)
public interface ValidationReport {
    /**
     * Get the Metadata object for the ODF document.
     * 
     * @see Metadata
     * @return the Metadata object.
     */
    public Metadata getMetadata();

    /**
     * The Manifest object for the ODF document.
     *
     * @see Manifest
     * @return the Manifest object for an ODF document.
     */
    public Manifest getManifest();

    /**
     * The Validation result for the ODF document.
     *
     * @see ValidationResult
     * @return The ValidationResult object for the ODF document.
     */
    public ValidationResult getValidationResult();

    /**
     * The Profile result for the ODF document, if a profile was used.
     * 
     * @see ProfileResult
     * @return
     */
    public ProfileResult getProfileResult();
}
