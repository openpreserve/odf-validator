package org.openpreservation.odf.pkg;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class OdfPackages {
    public static final String MIMETYPE = Constants.MIMETYPE;

    private OdfPackages() {
        throw new AssertionError("Utility class 'OdfPackages' should not be instantiated");
    }

    public static final ValidatingParser getValidatingParser() {
        return ValidatingParserImpl.getInstance();
    }

    public static final boolean isPackage(final Path toCheck) throws IOException {
        try (ZipFile zipFile = new ZipFile(toCheck.toFile())) {
            return true;
        } catch (ZipException e) {
            /**
             * No need to report this as an error as it simply means that the file is not a
             * ZIP
             */
        }
        return false;
    }
}
