package org.openpreservation.odf.validation;

import java.util.Set;

import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.xml.OdfXmlDocument;

public interface Profile {
    public String getId();
    public String getName();
    public String getDescription();
    public ProfileResult check(final OdfXmlDocument document) throws ParseException;
    public ProfileResult check(final OdfPackage odfPackage) throws ParseException;
    public Set<Rule> getRules();
}
