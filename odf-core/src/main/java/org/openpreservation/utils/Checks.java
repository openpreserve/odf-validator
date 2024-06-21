package org.openpreservation.utils;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Checks {
    public static final String NOT_NULL = "%s parameter: %s must not be null.";
    public static final String NOT_EMPTY = "String parameter: %s must not be empty.";
    public static final String GREATER_THAN_OR_EQUAL_TO = "Parameter: %s must be greater than or equal to %s.";

    private Checks() {
        throw new AssertionError("Should not be instantiated.");
    }

    public static final void existingFileCheck(final Path toValidate) throws FileNotFoundException {
        if (!Files.exists(toValidate)) {
            throw new FileNotFoundException(errMessage(toValidate.toString(), " does not exist."));
        } else if (Files.isDirectory(toValidate)) {
            throw new IllegalArgumentException(errMessage(toValidate.toString(), " is a directory."));
        }
    }

    public static final String errMessage(final String toValidate, final String subMess) {
        return String.format("Supplied Path parameter: %s %s", toValidate, subMess);
    }
}
