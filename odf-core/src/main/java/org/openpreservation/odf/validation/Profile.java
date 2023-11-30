package org.openpreservation.odf.validation;

import java.io.IOException;
import java.util.SortedSet;

import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.xml.OdfXmlDocument;

public interface Profile {
    public String getId();
    public String getName();
    public String getDescription();
    public ProfileResult check(final OdfXmlDocument document) throws IOException;
    public ProfileResult check(final OdfPackage odfPackage) throws IOException;
    public SortedSet<Rule> getRules();
}
