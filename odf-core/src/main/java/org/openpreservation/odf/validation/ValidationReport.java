package org.openpreservation.odf.validation;

import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.xml.Metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ValidationReportImpl.class )
public interface ValidationReport {
    /**
     * Get the Metadata object for the ODF document.
     * 
     * @see Metadata
     * @return the Metadata object.
     */
    @JsonIgnore
     public Metadata getMetadata();

    /**
     * The Manifest object for the ODF document.
     *
     * @see Manifest
     * @return the Manifest object for an ODF document.
     */
    @JsonIgnore
    public Manifest getManifest();

    /**
     * The Validation result for the ODF document.
     *
     * @see ValidationResult
     * @return The ValidationResult object for the ODF document.
     */
    @JsonProperty("validation_result")
    public ValidationResult getValidationResult();

    /**
     * The Profile result for the ODF document, if a profile was used.
     * 
     * @see ProfileResult
     * @return
     */
    @JsonProperty("profile_result")
    public ProfileResult getProfileResult();
}
