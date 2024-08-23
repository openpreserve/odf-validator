package org.openpreservation.odf.validation.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.Test;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Profile;
import org.openpreservation.odf.validation.ProfileResult;

public class ProfileImplTest {
    @Test
    public void testCheck() throws IOException, URISyntaxException, ParseException {
        Profile profile = Rules.getDnaProfile();
        PackageParser parser = OdfPackages.getPackageParser();
        OdfPackage pkg = parser.parsePackage(Paths.get(new File(TestFiles.EMPTY_ODS.toURI()).getAbsolutePath()));
        ProfileResult result = profile.check(pkg);
        assertNotNull(result);
        assertFalse(result.getValidationReport().isValid());
        assertFalse (result.isValid());
    }

    @Test
    public void testOf() {
        Profile profile = Rules.getDnaProfile();
        assertNotNull(profile);
    }
}
