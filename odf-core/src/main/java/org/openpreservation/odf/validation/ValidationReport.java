package org.openpreservation.odf.validation;

import java.util.List;

import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ValidationReportImpl.class )
public interface ValidationReport {
    /**
     * Get the name of the document being validated.
     *
     * @return the name of the document being validated
     */
    public String getFilename();
    /**
     * Determine if the OpenDocument is valid.
     * This depends on all of the validation results contained in the report.
     *
     * @return <code>true</code> if the OpenDocument is valid, otherwise <code>false</code>
     */
    @JsonProperty("is_valid")
    public boolean isValid();
    /**
     * Determine if the OpenDocument is encrypted.
     *
     * @return <code>true</code> if the OpenDocument is encrypted, otherwise <code>false</code>
     */
    @JsonProperty("is_encrypted")
    public boolean isEncrypted();
    /**
     * Get the detected format of the OpenDocument.
     *
     * @return the detected format of the OpenDocument
     */
    @JsonIgnore
    Version getVersion();
    /**
     * Get the detected format of the OpenDocument.
     *
     * @return the detected format of the OpenDocument
     */
    @JsonProperty("version")
    String getVersionString();
    /**
     * Get the detected format of the OpenDocument.
     *
     * @return the detected format of the OpenDocument
     */
    @JsonIgnore
    Formats getDetectedFormat();
    /**
     * Get the detected format of the OpenDocument.
     *
     * @return the detected format of the OpenDocument
     */
    @JsonProperty("format")
    String getFormat();
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
    @JsonProperty("validation_results")
    public List<ValidationResult> getValidationResults();

    /**
     * Get a list of all checks, from all Results contained in the file.
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
