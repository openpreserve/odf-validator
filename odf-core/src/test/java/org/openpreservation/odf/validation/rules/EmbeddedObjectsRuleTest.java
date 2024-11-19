package org.openpreservation.odf.validation.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.xml.OdfXmlDocument;

import com.helger.commons.io.resource.URLResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.AbstractSVRLMessage;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;

public class EmbeddedObjectsRuleTest {
    private final Rule rule = Rules.odf6();

    @Test
    public void testGetInstance() {
        final SchematronResourcePure schematron = EmbeddedObjectsRule.getInstance(Severity.INFO).schematron.schematron;
        assertTrue(schematron.isValidSchematron());
    }

    @Test
    public void testCheckNullXmlDoc() {
        OdfXmlDocument nullDoc = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    rule.check(nullDoc);
                });
    }

    @Test
    public void testCheckNullPackage() {
        OdfPackage nullPkg = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    rule.check(nullPkg);
                });
    }

    @Test
    public void testSchematronRuleEmbeddedFail() throws Exception {
        final SchematronResourcePure schematron = EmbeddedObjectsRule.getInstance(Severity.ERROR).schematron.schematron;
        final URL resource = TestFiles.OLE_EMBEDDED_XML;
        assertNotNull(resource);
        final SchematronOutputType schResult = schematron.applySchematronValidationToSVRL(new URLResource(resource));
        assertNotNull(schResult);
        List<AbstractSVRLMessage> results = SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(schResult);
        assertEquals(2, results.size());
    }

    @Test
    public void testSchematronNoEmbeddedPass() throws Exception {
        final SchematronResourcePure schematron = EmbeddedObjectsRule.getInstance(Severity.ERROR).schematron.schematron;
        final URL resource = ClassLoader.getSystemResource("org/openpreservation/odf/fmt/xml/content.xml");
        assertNotNull(resource);
        final SchematronOutputType schResult = schematron.applySchematronValidationToSVRL(new URLResource(resource));
        assertNotNull(schResult);
        List<AbstractSVRLMessage> results = SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(schResult);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckValidPackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertFalse("Valid Package should not return errors", results.hasErrors());
    }

    @Test
    public void testCheckEmbeddedPackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser
                .parsePackage(Paths.get(new File(TestFiles.OLE_EMBEDDED_PACKAGE.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertFalse("Valid Package should not return errors.", results.hasErrors());
        assertTrue("Valid Package should have info messages.", results.hasInfos());
    }
}
