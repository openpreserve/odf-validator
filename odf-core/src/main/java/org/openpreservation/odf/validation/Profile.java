package org.openpreservation.odf.validation;

import java.util.Set;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.PackageParser.ParseException;

public interface Profile {
    public String getId();

    public String getName();

    public String getDescription();

    public ProfileResult check(final OpenDocument document) throws ParseException;

    public Set<Rule> getRules();
}
