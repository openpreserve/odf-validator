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
import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.xml.OdfXmlDocument;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ExtensionMimeTypeRuleTest {
    private final Rule rule = Rules.odf4();
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ExtensionMimeTypeRule.class).verify();
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
    public void testCheckValidPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertFalse("Valid Package should not return errors", results.hasErrors());
    }

    @Test
    public void testCheckNotZipPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_FODS.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertTrue("Document XML should return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_4")).count() > 0).count());
    }

    @Test
    public void testCheckNotWellFormedPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.BADLY_FORMED_PKG.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertTrue("Badly formed package should return errors", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_4")).count() > 0).count());
    }

    @Test
    public void testCheckInvalidPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.MIME_EXTRA_ODS.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertFalse("Invalid extra headers for mimetype is OK.", results.hasErrors());
    }

    @Test
    public void testCheckNotOdsPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.DSIG_INVALID.toURI()).getAbsolutePath()));
        MessageLog results = rule.check(pkg);
        assertTrue("DSIG file has wrong MIME and extension", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_4")).count() > 0).count());
    }

    @Test
    public void testCheckBadExtPackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.ODF4_BAD_EXT.toURI()).getAbsolutePath()));
        assertEquals("Package should have spreadsheet MIME value", Formats.ODS.mime, pkg.getMimeType());
        MessageLog results = rule.check(pkg);
        assertTrue("Bad extension only but should be invalid", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_4")).count() > 0).count());
    }

    @Test
    public void testCheckBadMimePackage() throws IOException, URISyntaxException {
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.ODF4_BAD_MIME.toURI()).getAbsolutePath()));
        assertEquals("Package should have template MIME value", Formats.OTS.mime, pkg.getMimeType());
        assertTrue("Package should have spreadsheet extension.", pkg.getName().endsWith(Formats.ODS.extension));
        MessageLog results = rule.check(pkg);
        assertTrue("Bad extension only but should be invalid", results.hasErrors());
        assertEquals(1, results.getMessages().values().stream().filter(m -> m.stream().filter(e -> e.getId().equals("POL_4")).count() > 0).count());
    }
}
