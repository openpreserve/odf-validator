package org.openpreservation.odf.validation;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.xml.sax.SAXException;

public class OdfValidators {
    
    /**
     * Get a non-extended validating parser instance.
     *
     * @return a validating parser instance
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static final ValidatingParser getValidatingParser() throws ParserConfigurationException, SAXException {
        return ValidatingParserImpl.getInstance(false);
    }

    /**
     * Get either an extended or non-extended validating parser instance.
     *
     * @return a validating parser instance
     * @param isExtended whether to enable extended validation/conformance
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static final ValidatingParser getValidatingParser(final boolean isExtended) throws ParserConfigurationException, SAXException {
        return ValidatingParserImpl.getInstance(isExtended);
    }

    /**
     * Get a validating parser instance that implements extended validation/conformance.
     *
     * @return a validating parser instance
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static final ValidatingParser getExtendedValidatingParser() throws ParserConfigurationException, SAXException {
        return ValidatingParserImpl.getInstance(true);
    }

    /**
     * Check if a zip entry has a valid compression method.
     *
     * @param entry a zip entry to check
     * @return true if the compression method is valid, else false
     */
    public static boolean isCompressionValid(final ZipEntry entry) {
        return ValidatingParserImpl.isCompressionValid(entry);
    }

    /**
     * Create a mimimal validation report.
     *
     * @param filename a name for the report
     * @return the minimal validation report with the given name
     */
    public static final ValidationResult resultOf(final String name, final MessageLog messages) {
        return ValidationResultImpl.of(name, messages);
    }

    public static final OdfValidator getOdfValidator() {
        return OdfValidatorImpl.getInstance(false);
    }

    public static final OdfValidator getOdfValidator(final boolean isExtended) {
        return OdfValidatorImpl.getInstance(isExtended);
    }

    private OdfValidators() {
        throw new AssertionError("Utility class 'Validators' should not be instantiated");
    }
}
