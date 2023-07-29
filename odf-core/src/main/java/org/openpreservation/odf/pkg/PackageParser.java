package org.openpreservation.odf.pkg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public interface PackageParser {
    public OdfPackage parsePackage(Path toParse) throws IOException, ParserConfigurationException, SAXException;
    public OdfPackage parsePackage(File toParse) throws IOException, ParserConfigurationException, SAXException;
    public OdfPackage parsePackage(InputStream toParse) throws IOException, ParserConfigurationException, SAXException;
}
