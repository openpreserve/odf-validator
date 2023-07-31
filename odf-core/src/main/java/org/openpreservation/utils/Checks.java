package org.openpreservation.utils;

public class Checks {
    public static final String NOT_NULL = "%s parameter: %s must not be null.";
    public static final String NOT_EMPTY = "sTRING parameter: %s must not be empty.";
    public static final String GREATER_THAN_OR_EQUAL_TO = "Parameter: %s must be greater than or equal to %s.";

    private Checks() {
        throw new AssertionError("Should not be instantiated.");
    }
}
