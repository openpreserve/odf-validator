package org.openpreservation.odf.apps;

public final class DebugInfo {
    static final String javaVersion = System.getProperty("java.version");
    static final String javaVendor = System.getProperty("java.vendor");
    static final String javaHome = System.getProperty("java.home");
    static final String osName = System.getProperty("os.name");
    static final String osVersion = System.getProperty("os.version");
    static final String osArch = System.getProperty("os.arch");

    private final boolean debugFlag;
    private final boolean[] verbosity;

    private DebugInfo(boolean debugFlag, boolean[] verbosity) {
        this.debugFlag = debugFlag;
        this.verbosity = verbosity;
    }

    /**
     * Static factory method to create a DebugInfo instance.
     *
     * @param debugFlag whether debug output is enabled or not, set true to enable
     * @param verbosity a boolean array representing verbosity levels, the greater the length the more verbose
     * @return a new DebugInfo instance
     */
    public static final DebugInfo create(boolean debugFlag, boolean[] verbosity) {
        return new DebugInfo(debugFlag, verbosity);
    }

    /**
     * Output debug information to the console if debugFlag is set.
     */
    public void outputDebugInfo() {
        if (!this.debugFlag) {
            return;
        }
        ConsoleFormatter.info("Debug information:");
        ConsoleFormatter.info(String.format("  ODF Validator: %s", BuildVersionProvider.getVersionString()));
        ConsoleFormatter.info(String.format("  OS name: %s, version: %s, architecture: %s", osName, osVersion, osArch));
        ConsoleFormatter.info(String.format("  Java version: %s", javaVersion));
        // If the verbosity length is greater than 0, output additional JVM info
        if (verbosity.length > 0) {
            ConsoleFormatter.info(String.format("  Java vendor: %s", javaVendor));
            ConsoleFormatter.info(String.format("  Java home: %s", javaHome));
            ConsoleFormatter.info(String.format("  JVM Heap Size: %d", Runtime.getRuntime().totalMemory()));
            ConsoleFormatter.info(String.format("  JVM Max Heap Size: %d", Runtime.getRuntime().maxMemory()));
            ConsoleFormatter.info(String.format("  JVM Free Heap: %d", Runtime.getRuntime().freeMemory()));
        }
    }

}
