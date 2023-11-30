package org.openpreservation.odf.apps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.validation.Profile;
import org.openpreservation.odf.validation.ProfileResult;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.Validator;
import org.openpreservation.odf.validation.rules.Rules;
import org.xml.sax.SAXException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "odf-validator", mixinStandardHelpOptions = true, version = "odf-validator 0.1", description = "Validates Open Document Format spreadsheets.")
class CliValidator implements Callable<Integer> {
    private static final MessageFactory FACTORY = Messages
            .getInstance("org.openpreservation.odf.apps.messages.Messages");

    @Option(names = {"-p", "--profile"}, description = "Validate using extended Spreadsheet preservation profile.")
    private boolean profileFlag;
    @Parameters(paramLabel = "FILE", arity = "1..*", description = "A list of Open Document Format spreadsheet files to validate.")
    private File[] toValidateFiles;
    private final Validator validator = new Validator();
    private final Profile dnaProfile = Rules.getDnaProfile();
    private final Map<Path, ValidationReport> validationReports = new HashMap<>();
    private final Map<Path, ProfileResult> profileResults = new HashMap<>();
    private final Map<Path, MessageLog> appMessages = new HashMap<>();
    private final PackageParser parser = OdfPackages.getPackageParser();

    @Override
    public Integer call() {
        for (File file : this.toValidateFiles) {
            Path toValidate = file.toPath();
            if (this.profileFlag) {
                ConsoleFormatter.colourise(FACTORY.getInfo("APP-1", toValidate.toString()));
                profileResults.put(toValidate, profilePath(toValidate));
            } else {
                ConsoleFormatter.colourise(FACTORY.getInfo("APP-1", toValidate.toString()));
                validationReports.put(toValidate, validatePath(toValidate));
            }
        }
        return results();
    }

    private ValidationReport validatePath(final Path toValidate) {
        try {
            return validator.validate(toValidate);
        } catch (IllegalArgumentException | FileNotFoundException e) {
            this.logMessage(toValidate, Messages.getMessageInstance("APP-2", Severity.ERROR, e.getMessage()));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ProfileResult profilePath(final Path toProfile) {
        try {
            return dnaProfile.check(parser.parsePackage(toProfile));
        } catch (IllegalArgumentException | IOException e) {
            this.logMessage(toProfile, Messages.getMessageInstance("APP-2", Severity.ERROR, e.getMessage()));
        }
        return null;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CliValidator()).execute(args);
        System.exit(exitCode);
    }

    private Integer results() {
        Integer retStatus = 0;
        for (Map.Entry<Path, ValidationReport> entry : validationReports.entrySet()) {
            if (this.appMessages.containsKey(entry.getKey())) {
                for (List<Message> messages : this.appMessages.get(entry.getKey()).getMessages().values()) {
                    for (Message message : messages) {
                        ConsoleFormatter.colourise(message);
                        if (message.isError() || message.isFatal()) {
                            retStatus = 1;
                        }
                    }
                }
            }
            if (entry.getValue() != null) {
                retStatus = Math.max(retStatus, results(entry.getKey(), entry.getValue()));
            }
        }
        for (Map.Entry<Path, ProfileResult> entry : profileResults.entrySet()) {
            if (this.appMessages.containsKey(entry.getKey())) {
                for (List<Message> messages : this.appMessages.get(entry.getKey()).getMessages().values()) {
                    for (Message message : messages) {
                        ConsoleFormatter.colourise(message);
                        if (message.isError() || message.isFatal()) {
                            retStatus = 1;
                        }
                    }
                }
            }
            if (entry.getValue() != null) {
                retStatus = Math.max(retStatus, results(entry.getKey(), entry.getValue()));
            }
        }
        return retStatus;
    }

    private Integer results(final Path path, final ValidationReport report) {
        Integer retStatus = 0;
        ConsoleFormatter.colourise(FACTORY.getInfo("APP-4", path.toString(), "bold"));
        if (report.getMessages().isEmpty()) {
            ConsoleFormatter.info(FACTORY.getInfo("APP-3").getMessage());
        }
        for (Map.Entry<String, List<Message>> entry : report.documentMessages.getMessages().entrySet()) {
            for (Message message : entry.getValue()) {
                ConsoleFormatter.colourise(Paths.get(entry.getKey()), message);
                if (message.isError() || message.isFatal()) {
                    retStatus = 1;
                }
            }
        }
        if (report.documentMessages.hasErrors()) {
            ConsoleFormatter.error(String.format("NOT VALID, %d errors, %d warnings and %d info messages.",
                    report.documentMessages.getErrorCount(), report.documentMessages.getWarningCount(), report.documentMessages.getInfoCount()));
        } else if (report.documentMessages.hasWarnings()) {
            ConsoleFormatter.warn(String.format("VALID, no errors, %d warnings found and %d info messages.",
                    report.documentMessages.getWarningCount(), report.documentMessages.getInfoCount()));
        } else {
            ConsoleFormatter
                    .info(String.format("VALID, no errors, no warnings and %d info message found.", report.documentMessages.getInfoCount()));
        }
        ConsoleFormatter.newline();
        return retStatus;
    }

    private Integer results(final Path path, final ProfileResult report) {
        Integer retStatus = 0;
        ConsoleFormatter.colourise(FACTORY.getInfo("APP-5", this.dnaProfile.getName(), path.toString(), "bold"));
        for (Map.Entry<String, List<Message>> entry : report.getProfileMessages().getMessages().entrySet()) {
            for (Message message : entry.getValue()) {
                ConsoleFormatter.colourise(Paths.get(entry.getKey()), message);
                if (message.isError() || message.isFatal()) {
                    retStatus = 1;
                }
            }
        }
        if (report.getProfileMessages().hasErrors()) {
            ConsoleFormatter.error(String.format("NOT VALID, %d errors, %d warnings and %d info messages.",
                   report.getProfileMessages().getErrorCount(), report.getProfileMessages().getWarningCount(), report.getProfileMessages().getInfoCount()));
        } else if (report.getProfileMessages().hasWarnings()) {
            ConsoleFormatter.warn(String.format("VALID, no errors, %d warnings found and %d info messages.",
                    report.getProfileMessages().getWarningCount(), report.getProfileMessages().getInfoCount()));
        } else {
            ConsoleFormatter
                    .info(String.format("VALID, no errors, no warnings and %d info message found.", report.getProfileMessages().getInfoCount()));
        }
        ConsoleFormatter.newline();
        if (report.getValidationReport() != null) {
            retStatus = Math.max(retStatus, results(path, report.getValidationReport()));            
        }
        return retStatus;
    }

    private final void logMessage(final Path path, final Message message) {
        this.appMessages.putIfAbsent(path, Messages.messageLogInstance());
        this.appMessages.get(path).add(path.toString(), message);
    }
}
