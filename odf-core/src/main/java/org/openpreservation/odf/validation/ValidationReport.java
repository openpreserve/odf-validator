package org.openpreservation.odf.validation;

import java.util.List;

import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.validation.messages.Message.Severity;
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

    /**
     * Get a list of all messages, from all Results contained in the file.
     *
     * @return 
     */
    @JsonIgnore
    public List<Check> getChecks();

    /**
     * Do any of the contained results contain the specified severity?
     *
     * @return true if any of the validation result messages contain the specified severity.
     */
    @JsonIgnore
    public boolean hasSeverity(Severity severity);
}
