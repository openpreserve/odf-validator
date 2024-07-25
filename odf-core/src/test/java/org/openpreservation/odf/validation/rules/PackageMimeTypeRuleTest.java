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
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.xml.OdfXmlDocument;

import nl.jqno.equalsverifier.EqualsVerifier;

public class PackageMimeTypeRuleTest {
    private final Rule rule = Rules.odf3();
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(PackageMimeTypeRule.class).verify();
    }

    @Test
    public void testGetInstance() {
        assertNotNull("Returned Rule should not be null", rule);
    }

    @Test
    public void testCheckNullXmlDoc() {
        OdfXmlDocument nullDoc = null;
        assertThrows("UnsupportedOperationException expected",
        UnsupportedOperationException.class,
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
    public void testCheckValidPackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertFalse("Valid Package should not return errors", results.hasErrors());
    }

    @Test
    public void testCheckNotZipPackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_FODS.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertTrue("Document XML should return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_3")).count() > 0).count());
    }

    @Test
    public void testCheckNotWellFormedPackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.BADLY_FORMED_PKG.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertTrue("Badly formed package should return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_3")).count() > 0).count());
    }

    @Test
    public void testCheckInvalidPackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.MIME_EXTRA_ODS.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertFalse("Invalid extra headers for mimetype is OK.", results.hasErrors());
    }

    @Test
    public void testCheckNoMimePackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.NO_MIME_ROOT_ODS.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertTrue("No mimetype with root entry (invalid) return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_3")).count() > 0).count());
    }

    @Test
    public void testCheckNoMimeNoRootPackage() throws IOException, URISyntaxException, ParseException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.NO_MIME_NO_ROOT_ODS.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertTrue("No mimetype with no root entry (valid) should return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_3")).count() > 0).count());
    }
}

