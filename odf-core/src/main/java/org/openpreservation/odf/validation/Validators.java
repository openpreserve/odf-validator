package org.openpreservation.odf.validation;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.zip.ZipEntry;
import org.openpreservation.odf.document.OpenDocument;
import org.xml.sax.SAXException;

import net.sf.saxon.lib.Validation;

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

    public static final ValidationReport reportOf(final String name) {
        return ValidationReport.of(name);
    }

    public static final ValidationReport reportOf(final String name, final OpenDocument document) {
        return ValidationReport.of(name, document);
    }

    private Validators() {
        throw new AssertionError("Utility class 'Validators' should not be instantiated");
    }
}
