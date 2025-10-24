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

    public static final DebugInfo create(boolean debugFlag, boolean[] verbosity) {
        return new DebugInfo(debugFlag, verbosity);
    }
  
    void outputDebugInfo() {
        if (!this.debugFlag) {
            return;
        }
        ConsoleFormatter.info("Debug information:");
        ConsoleFormatter.info(String.format("  OS name: %s", osName));
        ConsoleFormatter.info(String.format("  OS version: %s", osVersion));
        ConsoleFormatter.info(String.format("  OS architecture: %s", osArch));
        ConsoleFormatter.info(String.format("  Java version: %s", javaVersion));
        ConsoleFormatter.info(String.format("  Java vendor: %s", javaVendor));
        ConsoleFormatter.info(String.format("  Java home: %s", javaHome));
        if (verbosity.length > 0) {
          ConsoleFormatter.info(String.format("  JVM Heap Size: %d", Runtime.getRuntime().totalMemory()));
          ConsoleFormatter.info(String.format("  JVM Max Heap Size: %d", Runtime.getRuntime().maxMemory()));
          ConsoleFormatter.info(String.format("  JVM Free Heap: %d", Runtime.getRuntime().freeMemory()));
          ConsoleFormatter.info(String.format("  ODF Tools version: %s", BuildVersionProvider.getVersionString()));
        }
    }

}
