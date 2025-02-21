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
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.rules.MacroRule;
import org.openpreservation.odf.validation.rules.Rules;

import com.helger.commons.io.resource.URLResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.AbstractSVRLMessage;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;

public class MacrosTest {
    private final Rule rule = Rules.odf8();

    @Test
    public void testGetInstance() {
        final SchematronResourcePure schematron = MacroRule.getInstance(Severity.ERROR).schematron.schematron;
        assertTrue(schematron.isValidSchematron());
    }

    @Test
    public void testCheckNullXmlDoc() {
        OpenDocument nullDoc = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    rule.check(nullDoc);
                });
    }

    @Test
    public void testSchematronRuleMacroFail() throws Exception {
        final SchematronResourcePure schematron = MacroRule.getInstance(Severity.ERROR).schematron.schematron;
        final URL resource = TestFiles.MACRO_XML;
        assertNotNull(resource);
        final SchematronOutputType schResult = schematron.applySchematronValidationToSVRL(new URLResource(resource));
        assertNotNull(schResult);
        List<AbstractSVRLMessage> results = SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(schResult);
        assertEquals(1, results.size());
    }

    @Test
    public void testSchematronNoMacroPass() throws Exception {
        final SchematronResourcePure schematron = MacroRule.getInstance(Severity.ERROR).schematron.schematron;
        final URL resource = ClassLoader.getSystemResource("org/openpreservation/odf/fmt/xml/content.xml");
        assertNotNull(resource);
        final SchematronOutputType schResult = schematron.applySchematronValidationToSVRL(new URLResource(resource));
        assertNotNull(schResult);
        List<AbstractSVRLMessage> results = SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(schResult);
        assertEquals(0, results.size());
    }

    @Test
    public void testPackageMacroFail() throws Exception {
        MessageLog messages = Utils.getMessages(TestFiles.MACRO_PACKAGE, rule);
        assertNotNull(messages);
        assertEquals(2, messages.getErrors().size());
        assertEquals(2, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-8")).count() > 0).count());
    }

    @Test
    public void testPackageNoMacroPass() throws Exception {
        MessageLog messages = Utils.getMessages(TestFiles.EMPTY_ODS, rule);
        assertNotNull(messages);
        assertEquals(0, messages.getErrors().size());
    }

    @Test
    public void testPackageStarBasicFail() throws Exception {
        MessageLog messages = Utils.getMessages(TestFiles.STAR_BASIC, rule);
        assertNotNull(messages);
        assertEquals(1, messages.getErrors().size());
        assertEquals(1, messages.getMessages().values().stream()
                .filter(m -> m.stream().filter(e -> e.getId().equals("POL-8")).count() > 0).count());
    }
}
