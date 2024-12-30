package org.openpreservation.odf.validation;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.zip.ZipEntry;
import org.xml.sax.SAXException;

public class Validators {

    /**
     * Get a validating parser instance.
     *
     * @return a validating parser instance
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static final ValidatingParser getValidatingParser() throws ParserConfigurationException, SAXException {
        return ValidatingParserImpl.getInstance();
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
     * @param name a name for the report
     * @return the minimal validation report with the given name
     */
    public static final ValidationResult reportOf(final String name) {
        return ValidationResultImpl.of(name);
    }

    private Validators() {
        throw new AssertionError("Utility class 'Validators' should not be instantiated");
    }
}
