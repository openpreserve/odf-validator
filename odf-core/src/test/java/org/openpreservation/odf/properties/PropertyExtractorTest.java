package org.openpreservation.odf.properties;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.ValidatingParser;
import org.openpreservation.odf.validation.ValidationReport;
import org.xml.sax.SAXException;

public class PropertyExtractorTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testExtractProperties() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        ValidatingParser parser = OdfPackages.getValidatingParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(ClassLoader.getSystemResource(TestFiles.MACRO_ODS).toURI()));
        ValidationReport report = parser.validatePackage(pkg);
        PropertyExtractor extractor = new PropertyExtractorImpl();
        List<Property> props = extractor.extractProperties(pkg, report);
        assertTrue(props.contains(PropertyImpl.create("Macros", "ODS-8")));
    }
}
