package org.openpreservation.odf.validation;

import java.util.Set;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.PackageParser.ParseException;

public interface Profile {
    /**
     * Get the id of the profile.
     *
     * @return the id of the profile
     */
    public String getId();

    /**
     * Get the name of the profile.
     *
     * @return the name of the profile
     */
    public String getName();

    /**
     * Get a short description of the profile.
     *
     * @return the description of the profile
     */
    public String getDescription();

    /**
     * Check the given document against the profile.
     *
     * @see ValidationReport
     * @param document the document to check
     * @return a ValidationReport containing the results of the check
     * @throws ParseException if there's a problem parsing the document
     */
    public ValidationReport check(final OpenDocument document) throws ParseException;

    /**
     * Get the set of rules that comprise the profile.
     *
     * @see Rule
     * @return the set of rules that comprise the profile
     */
    public Set<Rule> getRules();
}
