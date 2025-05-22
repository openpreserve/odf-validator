package org.openpreservation.odf.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.PackageParser.ParseException;

public interface OdfValidator {
    /**
     * Validate an ODF document from a path.
     *
     * @param toValidate the path to the document to validate
     * @return the <code>ValidationResult</code> for the document
     * @throws ParseException if the document cannot be parsed
     * @throws FileNotFoundException if the document cannot be found
     */
    public ValidationReport validate(final Path toValidate) throws ParseException, FileNotFoundException;

    /**
     * Validate a path, only files of a specific ODF format are allowed.
     * This method will check if the file is an ODF document and if it is of the
     * specified format. If the file is not an ODF document, it will return a
     * validation result indicating that the file is not an ODF document.
     * If the file is an ODF document but not of the specified format, it will
     * return a validation result indicating that the file is not of the expected
     *.
     * @param toValidate the path to the document to validate
     * @param legal the expected format of the document
     *
     * @return the validation result
     * @throws ParseException if the document cannot be parsed
     * @throws FileNotFoundException if the document cannot be found
     */
    public ValidationReport validate(final Path toValidate, final Formats legal) throws ParseException, FileNotFoundException;

    /**
     * Validate an OpenDocument.
     *
     * @param toValidate the OpenDocument to validate
     * @return the <code>ValidationResult</code> for the document
     * @throws IOException if the document cannot be read
     */
    public ValidationResult validate(final OpenDocument toValidate) throws IOException;

    /**
     * Profile check a document using a specific profile.
     *
     * @param toProfile the path to the document to profile
     * @param profile the profile to use
     * @return the <code>ProfileResult</code> for the document
     * @throws ParseException if the document cannot be parsed
     * @throws FileNotFoundException if the document cannot be found
     */
    public ValidationReport profile(final Path toProfile, final Profile profile) throws ParseException, FileNotFoundException;
}
