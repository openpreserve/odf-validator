package org.openpreservation.odf.validation.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.rules.Rules;
import org.openpreservation.odf.validation.rules.SchematronRule;

import com.helger.commons.io.resource.URLResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.AbstractSVRLMessage;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;

public class ExternalDataTest {
    @Test
    public void testGetInstance() {
        final SchematronResourcePure schematron = ((SchematronRule) Rules.odf5()).schematron;
        assertTrue(schematron.isValidSchematron());
    }

    @Test
    public void testCheckNullXmlDoc() {
        Rule rule = Rules.odf5();
        OpenDocument nullDoc = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    rule.check(nullDoc);
                });
    }


    @Test
    public void testSchematronExternalDataFail() throws Exception {
        final SchematronResourcePure schematron = ((SchematronRule) Rules.odf5()).schematron;
        final URL resource = TestFiles.SCHEMATRON_CHECKER_XML;
        assertNotNull(resource);
        final SchematronOutputType schResult = schematron.applySchematronValidationToSVRL(new URLResource(resource));
        assertNotNull(schResult);
        List<AbstractSVRLMessage> results = SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(schResult);
        assertEquals(1, results.size());
    }

    @Test
    public void testSchematronExternalDataPass() throws Exception {
        final SchematronResourcePure schematron = ((SchematronRule) Rules.odf5()).schematron;
        final URL resource = ClassLoader.getSystemResource("org/openpreservation/odf/fmt/xml/content.xml");
        assertNotNull(resource);
        final SchematronOutputType schResult = schematron.applySchematronValidationToSVRL(new URLResource(resource));
        assertNotNull(schResult);
        List<AbstractSVRLMessage> results = SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(schResult);
        assertEquals(0, results.size());
    }

    @Test
    public void testPackageExternalDataFail() throws Exception {
        MessageLog messages = Utils.getMessages(TestFiles.SCHEMATRON_CHECKER_ODS, Rules.odf5());
        assertNotNull(messages);
        assertEquals(0, messages.getErrors().size());
        assertEquals(1, messages.getInfos().size());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-5")).count() > 0).count());
    }

    @Test
    public void testPackageExternalDataPass() throws Exception {
        MessageLog messages = Utils.getMessages(TestFiles.MACRO_PACKAGE, Rules.odf5());
        assertNotNull(messages);
        assertEquals(0, messages.getErrors().size());
    }
}
