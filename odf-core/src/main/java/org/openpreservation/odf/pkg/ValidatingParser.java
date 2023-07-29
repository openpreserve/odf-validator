package org.openpreservation.odf.pkg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.validation.ValidationReport;
import org.xml.sax.SAXException;

public interface ValidatingParser extends PackageParser {
    public ValidationReport validatePackage(Path toValidate)
            throws IOException, ParserConfigurationException, SAXException;

    public ValidationReport validatePackage(File toValidate)
            throws IOException, ParserConfigurationException, SAXException;

    public ValidationReport validatePackage(InputStream toValidate, final String name)
            throws IOException, ParserConfigurationException, SAXException;
}
