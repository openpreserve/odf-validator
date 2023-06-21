package org.openpreservation.odf;

import java.nio.file.Path;

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

    public static final void error(final String message) {
        colourise(message, COL_ERR);
    }

    public static final void info(final String message) {
        colourise(message, COL_INFO);
    }

    public static final void warn(final String message) {
        colourise(message, COL_WARN);
    }

    public static final void colourise(final String message, final String colour) {
        System.out.println(Ansi.AUTO.string(String.format("@|%s %s |@", colour, message)));
    }

    public static final void newline() {
        System.out.println();
    }

    public static final void colourise(final Message message) {
        colourise(message, colourFromMessage(message));
    }

    public static final void colourise(final Path path, final Message message) {
        colourise(path, message, colourFromMessage(message));
    }

    public static final void colourise(final Message message, final String colour) {
        final String formatted = String.format("%s: [%s] %s", message.getId(), message.getSeverity(),
                message.getMessage());
        colourise(formatted, colour);
    }

    public static final void colourise(final Path path, final Message message, final String colour) {
        final String formatted = String.format("%s: %s [%s] %s", path, message.getId(), message.getSeverity(),
                message.getMessage());
        colourise(formatted, colour);
    }

    private static final String colourFromMessage(final Message message) {
        if (message.isError() || message.isFatal()) {
            return COL_ERR;
        } else if (message.isWarning()) {
            return COL_WARN;
        } else {
            return COL_INFO;
        }
    }
}
