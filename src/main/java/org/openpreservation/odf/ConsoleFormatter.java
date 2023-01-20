package org.openpreservation.odf;

import org.openpreservation.messages.Message;

import picocli.CommandLine.Help.Ansi;

public enum ConsoleFormatter {
    INSTANCE;

    public static final String COL_GREEN = "green";
    public static final String COL_RED = "red";
    public static final String COL_YELLOW = "yellow";
    public static final String COL_ERR = COL_RED;
    public static final String COL_INFO = COL_GREEN;
    public static final String COL_WARN = COL_YELLOW;

    public static void error(final String message) {
        colourise(message, COL_ERR);
    }

    public static void info(final String message) {
        colourise(message, COL_INFO);
    }

    public static void warn(final String message) {
        colourise(message, COL_WARN);
    }

    public static void colourise(final String message, final String colour) {
        System.out.println(Ansi.AUTO.string(String.format("@|%s %s |@", colour, message)));
    }

    public static void colourise(final Message message) {
        if (message.isError() || message.isFatal()) {
            colourise(message, COL_ERR);
        } else if (message.isWarning()) {
            colourise(message, COL_WARN);
        } else {
            colourise(message, COL_INFO);
        }
    }

    public static void colourise(final Message message, final String colour) {
        final String formatted = String.format("%s: [%s] %s", message.getId(), message.getSeverity(),
                message.getMessage());
        colourise(formatted, colour);
    }
}
