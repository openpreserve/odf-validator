package org.openpreservation.odf.validation;

import java.util.List;

import org.openpreservation.odf.document.OpenDocument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ValidationReports {
    private ValidationReports() {
        // Utility class, no instantiation allowed
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Create a new {@link ValidationReport} instance.
     *
     * @param filename the name of the document being validated
     * @param document the OpenDocument being validated
     * @param validationResults the list of validation results for the report
     * @return a new {@link ValidationReport} instance
     */
    public static ValidationReport reportFromValues(final String filename,
                                                    OpenDocument document,
                                                    final List<ValidationResult> validationResults) {
        return ValidationReportImpl.of(filename, document, validationResults);
    }

    /**
     * Convert a {@link ValidationReport} to a JSON string.
     *
     * @param report the validation report to convert
     * @return a JSON string representation of the validation report
     * @throws JsonProcessingException
     *         if there is an error during JSON processing
     */
    public static String getJsonReport(final ValidationReport report) throws JsonProcessingException {
        var jsonMapper = new ObjectMapper().registerModule(new JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT);
        return jsonMapper.writeValueAsString(report);
    }

    /**
     * Convert a {@link ValidationReport} to an XML string.
     *
     * @param report the validation report to convert
     * @return an XML string representation of the validation report
     * @throws JsonProcessingException
     *         if there is an error during to XML processing
     */
    public static String getXmlReport(final ValidationReport report) throws JsonProcessingException {
        var xmlMapper = new XmlMapper().registerModule(new JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT);
        return xmlMapper.writeValueAsString(report);
    }

    public static String getReport(final ValidationReport report,
                                final FormatOption format) throws JsonProcessingException {
        if (format == FormatOption.JSON) {
            return getJsonReport(report);
        } else if (format == FormatOption.XML) {
            return getXmlReport(report);
        } else {
            return report.toString();
        }
    }

    public static enum FormatOption {
        JSON, XML, TEXT
    }
}
