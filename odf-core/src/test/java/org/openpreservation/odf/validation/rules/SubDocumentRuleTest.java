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

import org.junit.Test;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.xml.OdfXmlDocument;

import nl.jqno.equalsverifier.EqualsVerifier;

public class SubDocumentRuleTest {
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(SubDocumentRule.class).verify();
    }

    @Test
    public void testGetInstance() {
        Rule rule = SubDocumentRule.getInstance();
        assertNotNull("Returned Rule should not be null", rule);
    }

    @Test
    public void testCheckNullXmlDoc() {
        Rule rule = SubDocumentRule.getInstance();
        OdfXmlDocument nullDoc = null;
        assertThrows("UnsupportedOperationException expected",
        UnsupportedOperationException.class,
                () -> {
                    rule.check(nullDoc);
                });
    }

    @Test
    public void testCheckNullPackage() {
        Rule rule = SubDocumentRule.getInstance();
        OdfPackage nullPkg = null;
        assertThrows("NullPointerException expected",
        NullPointerException.class,
                () -> {
                    rule.check(nullPkg);
                });
    }

    @Test
    public void testCheckValidPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath()));
        Rule rule = SubDocumentRule.getInstance();
        MessageLog results = rule.check(pkg);
        assertTrue("Valid Package should not return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("ODF_10")).count() > 0).count());
    }

    @Test
    public void testCheckNotZipPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_FODS.toURI()).getAbsolutePath()));
        Rule rule = SubDocumentRule.getInstance();
        MessageLog results = rule.check(pkg);
        assertFalse("Document XML should NOT return errors", results.hasErrors());
    }

    @Test
    public void testCheckNotWellFormedPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.BADLY_FORMED_PKG.toURI()).getAbsolutePath()));
        Rule rule = Rules.odf10();
        MessageLog results = rule.check(pkg);
        assertFalse("Badly formed package does not contain digital signatures.", results.hasErrors());
    }

    @Test
    public void testCheckInvalidPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.MIME_EXTRA_ODS.toURI()).getAbsolutePath()));
        Rule rule = SubDocumentRule.getInstance();
        MessageLog results = rule.check(pkg);
        assertTrue("Invalid extra headers for contains an empty subdocument.", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("ODF_10")).count() > 0).count());
    }

    @Test
    public void testCheckValidDsigPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.DSIG_VALID.toURI()).getAbsolutePath()));
        Rule rule = SubDocumentRule.getInstance();
        MessageLog results = rule.check(pkg);
        assertTrue("File contains an empty sub document.", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("ODF_10")).count() > 0).count());
    }
}