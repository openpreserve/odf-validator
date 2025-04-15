package org.openpreservation.odf.validation.rules;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Profile;
import org.openpreservation.odf.validation.ValidationReport;
import org.xml.sax.SAXException;

public class ProfileImplTest {
    @Test
    public void testCheck() throws URISyntaxException, ParseException, ParserConfigurationException, SAXException , FileNotFoundException {
        Profile profile = Rules.getDnaProfile();
        OpenDocument doc = Utils.getDocument(TestFiles.EMPTY_ODS);
        ValidationReport report = profile.check(doc);
        assertNotNull(report);
        assertTrue(report.getValidationResults().get(0).isValid());
    }

    @Test
    public void testOf() throws ParserConfigurationException, SAXException {
        Profile profile = Rules.getDnaProfile();
        assertNotNull(profile);
    }
}
