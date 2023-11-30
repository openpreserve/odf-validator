package org.openpreservation.odf.validation.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ValidPackageRuleTest {
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ValidPackageRule.class).withIgnoredFields("validatingParser").verify();
    }

    @Test
    public void testGetInstance() throws ParserConfigurationException, SAXException {
        Rule rule = ValidPackageRule.getInstance();
        assertNotNull("Returned Rule should not be null", rule);
    }

    @Test
    public void testCheckNullXmlDoc() throws ParserConfigurationException, SAXException {
        Rule rule = ValidPackageRule.getInstance();
        OdfXmlDocument nullDoc = null;
        assertThrows("UnsupportedOperationException expected",
                UnsupportedOperationException.class,
                () -> {
                    rule.check(nullDoc);
                });
    }

    @Test
    public void testCheckNullPackage() throws ParserConfigurationException, SAXException {
        Rule rule = ValidPackageRule.getInstance();
        OdfPackage nullPkg = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    rule.check(nullPkg);
                });
    }

    @Test
    public void testCheckValidPackage()
            throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath()));
        Rule rule = ValidPackageRule.getInstance();
        MessageLog results = rule.check(pkg);
        assertFalse("Valid Package should not return errors", results.hasErrors());
    }

    @Test
    public void testCheckNotZipPackage()
            throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_FODS.toURI()).getAbsolutePath()));
        Rule rule = ValidPackageRule.getInstance();
        MessageLog results = rule.check(pkg);
        assertTrue("Document XML should return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("ODF_2")).count() > 0).count());
    }

    @Test
    public void testCheckNotWellFormedPackage()
            throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.BADLY_FORMED_PKG.toURI()).getAbsolutePath()));
        Rule rule = Rules.odf2();
        MessageLog results = rule.check(pkg);
        assertTrue("Document XML should return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("ODF_2")).count() > 0).count());
    }

    @Test
    public void testCheckInvalidPackage()
            throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.MIME_EXTRA_ODS.toURI()).getAbsolutePath()));
        Rule rule = ValidPackageRule.getInstance();
        MessageLog results = rule.check(pkg);
        assertTrue("Document XML should return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("ODF_2")).count() > 0).count());
    }
}
