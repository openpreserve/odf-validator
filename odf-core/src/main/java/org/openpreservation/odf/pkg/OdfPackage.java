package org.openpreservation.odf.pkg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.validation.ValidationReport;
import org.xml.sax.SAXException;

public interface OdfPackage {
    public String getMimeEntry();

    public boolean hasMimeEntry();

    public ValidationReport validate() throws IOException, ParserConfigurationException, SAXException;

    public Set<Path> getEntryPaths();

    public InputStream getEntryStream(Path entryPath) throws IOException;
}
