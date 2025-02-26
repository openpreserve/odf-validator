package org.openpreservation.odf.validation.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.validation.messages.MessageLog;

import com.helger.commons.io.resource.URLResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.AbstractSVRLMessage;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;

public class ContentTest {
    @Test
    public void testGetInstance() {
        final SchematronResourcePure schematron = ((SchematronRule) Rules.odf7()).schematron;
        assertTrue(schematron.isValidSchematron());
    }

    @Test
    public void testCheckNullXmlDoc() {
        Rule rule = Rules.odf7();
        OpenDocument nullDoc = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    rule.check(nullDoc);
                });
    }

    @Test
    public void testSchematronContentFail() throws Exception {
        final SchematronResourcePure schematron = ((SchematronRule) Rules.odf7()).schematron;
        final URL resource = ClassLoader.getSystemResource("org/openpreservation/odf/fmt/xml/content.xml");
        assertNotNull(resource);
        final SchematronOutputType schResult = schematron.applySchematronValidationToSVRL(new URLResource(resource));
        assertNotNull(schResult);
        List<AbstractSVRLMessage> results = SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(schResult);
        assertEquals(1, results.size());
    }

    @Test
    public void testSchematronContentPass() throws Exception {
        final SchematronResourcePure schematron = ((SchematronRule) Rules.odf7()).schematron;
        final URL resource = TestFiles.SCHEMATRON_CHECKER_XML;
        assertNotNull(resource);
        final SchematronOutputType schResult = schematron.applySchematronValidationToSVRL(new URLResource(resource));
        assertNotNull(schResult);
        List<AbstractSVRLMessage> results = SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(schResult);
        assertEquals(0, results.size());
    }

    @Test
    public void testPackageContentFail() throws FileNotFoundException, ParseException, URISyntaxException {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_ODS, Rules.odf7());
        assertNotNull(messages);
        assertEquals(1, messages.getWarnings().size());
    }

    @Test
    public void testPackageContentPass() throws FileNotFoundException, ParseException, URISyntaxException {
        MessageLog messages = Utils.getMessages(TestFiles.SCHEMATRON_CHECKER_ODS, Rules.odf7());
        assertNotNull(messages);
        assertEquals(0, messages.getErrors().size());
    }
}
