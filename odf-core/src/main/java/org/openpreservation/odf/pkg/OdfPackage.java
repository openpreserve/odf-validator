package org.openpreservation.odf.pkg;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.validation.ValidationReport;
import org.xml.sax.SAXException;

public interface OdfPackage {
    public String getMimeEntry();

    public boolean hasMimeEntry();

    public ValidationReport validate() throws IOException, ParserConfigurationException, SAXException;
}
