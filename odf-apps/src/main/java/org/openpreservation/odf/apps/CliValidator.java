package org.openpreservation.odf.apps;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Check;
import org.openpreservation.odf.validation.OdfValidator;
import org.openpreservation.odf.validation.OdfValidators;
import org.openpreservation.odf.validation.Profile;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.ValidationReports;
import org.openpreservation.odf.validation.ValidationResult;
import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.messages.MessageFactory;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Parameter.ParameterList;
import org.openpreservation.odf.validation.rules.Rules;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "odf-validator", mixinStandardHelpOptions = true, versionProvider = BuildVersionProvider.class, description = "Validates Open Document Format spreadsheets.")
class CliValidator implements Callable<Integer> {
    private static final MessageFactory FACTORY = Messages
            .getInstance("org.openpreservation.odf.apps.messages.Messages");

    @Option(names = { "-p", "--profile" }, description = "Validate using extended Spreadsheet preservation profile.")
    private boolean profileFlag;
    @Option(names = { "-d", "--debug" }, description = "Enable debug output.")
    private boolean debugFlag;
    @Option(names = { "-v" }, description = { "Specify multiple -v options to increase verbosity.",
                                              "For example, `-v -v -v` or `-vvv`"})
    private boolean[] verbosity;
    @Parameters(paramLabel = "FILE", arity = "1..*", description = "A list of Open Document Format spreadsheet files to validate.")
    private File[] toValidateFiles;
    @Option(names = { "--format" }, description = "Output results as TEXT, JSON or XML.", defaultValue = "TEXT")
    private ValidationReports.FormatOption format = ValidationReports.FormatOption.TEXT;
    private final OdfValidator validator = OdfValidators.getOdfValidator();
    private MessageLog appMessages = Messages.messageLogInstance();
    private DebugInfo debugInfo;

    @Override
    public Integer call() throws JsonProcessingException {
        Integer retStatus = 0;
        debugInfo = DebugInfo.create(this.debugFlag, this.verbosity != null ? this.verbosity : new boolean[0]);
        debugInfo.outputDebugInfo();
        for (File file : this.toValidateFiles) {
            Path toValidate = file.toPath();
            this.appMessages = Messages.messageLogInstance();
            ConsoleFormatter.colourise(FACTORY.getInfo("APP-1", Messages.parameterListInstance().add("file", toValidate.toString())));
            try {
                ValidationReport validationResult = (!this.profileFlag) ? validatePath(toValidate) : profilePath(toValidate);
                if (validationResult != null) {
                    retStatus = outputValidationReport(toValidate, validationResult, this.format);
                }
            } catch (Throwable t) {
                ConsoleFormatter.colourise(toValidate, FACTORY.getFatal("SYS-4", Messages.parameterListInstance().add("file", toValidate.toString())));
                ConsoleFormatter.error(String.format("An unexpected error occurred during validation: %s", t.toString()));
                debugInfo.outputDebugInfo();
                throw t;
            }
            if (this.appMessages.hasErrors()) {
                retStatus = Math.max(retStatus,
                        processCheckList(this.appMessages.getChecks()));
            }
        }
        debugInfo.outputDebugInfo();
        return retStatus;
    }

    private ValidationReport validatePath(final Path toValidate) {
        try {
            return validator.validate(toValidate);
        } catch (IllegalArgumentException | FileNotFoundException e) {
            this.logMessage(toValidate, Messages.getMessageInstance("APP-2", Severity.ERROR, e.getMessage()));
        } catch (ParseException e) {
            this.logMessage(toValidate, Messages.getMessageInstance("SYS-1", Severity.ERROR,
                    "Package could not be parsed, due to an exception.", e.getMessage()));
        }
        return null;
    }

    private ValidationReport profilePath(final Path path) {
        try {
            final Profile dnaProfile = Rules.getDnaProfile();
            return validator.profile(path, dnaProfile);
        } catch (IllegalArgumentException | FileNotFoundException e) {
            this.logMessage(path, Messages.getMessageInstance("APP-2", Severity.ERROR, e.getMessage()));
        } catch (ParseException | ParserConfigurationException | SAXException e) {
            this.logMessage(path, Messages.getMessageInstance("SYS-1", Severity.ERROR,
                    "Package could not be parsed, due to an exception.", e.getMessage()));
        }
        return null;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CliValidator()).execute(args);
        System.exit(exitCode);
    }

    private Integer processCheckList(final List<Check> checks) {
        Integer status = 0;
        for (Check check : checks) {
            ConsoleFormatter.colourise(check.getMessage());
            if (check.getMessage().isFatal()) {
                status = 1;
            }
        }
        return status;
    }

    private static Integer outputValidationReport(final Path path, final ValidationReport report, ValidationReports.FormatOption format) throws JsonProcessingException {
        ParameterList parameters = Messages.parameterListInstance().add("file", path.toString()).add("format", "bold");
        ConsoleFormatter.colourise(FACTORY.getInfo("APP-4", parameters));
        if (report.getChecks().isEmpty()) {
            ConsoleFormatter.info(FACTORY.getInfo("APP-3").getTitle());
        }

        Integer status = report.hasSeverity(Severity.FATAL) || report.hasSeverity(Severity.ERROR)  ? 1 : 0;
        MessageLog profileMessages = Messages.messageLogInstance();
        for (ValidationResult result : report.getValidationResults()) {
            profileMessages.add(result.getMessageLog().getMessages());
        }
        if (format == ValidationReports.FormatOption.TEXT)
            outputText(report);
        else
            ConsoleFormatter.info(ValidationReports.getReport(report, format));
        outputSummary(report.isEncrypted(), profileMessages);
        return status;
    }

    private static void outputText(final ValidationReport report) {
        for (ValidationResult result : report.getValidationResults()) {
            for (Map.Entry<String, List<Message>> entry : result.getMessageLog().getMessages().entrySet()) {
                outputMessage(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void outputMessage(final String path, final List<Message> messages) {
        for (Message message : messages) {
            ConsoleFormatter.colourise(Paths.get(path), message);
        }
    }

    private static void outputSummary(final boolean isEncrypted, final MessageLog messageLog) {
        if (isEncrypted) {
            ConsoleFormatter.error(String.format(
                    "INCOMPLETE encrypted entries are not supported, %d errors, %d warnings and %d info messages.",
                    messageLog.getErrorCount(), messageLog.getWarningCount(), messageLog.getInfoCount()));
        } else if (messageLog.hasErrors()) {
            ConsoleFormatter.error(String.format("NOT VALID, %d errors, %d warnings and %d info messages.",
                    messageLog.getErrorCount(), messageLog.getWarningCount(), messageLog.getInfoCount()));
        } else if (messageLog.hasWarnings()) {
            ConsoleFormatter.warn(String.format("VALID, no errors, %d warnings found and %d info messages.",
                    messageLog.getWarningCount(), messageLog.getInfoCount()));
        } else {
            ConsoleFormatter
                    .info(String.format("VALID, no errors, no warnings and %d info message found.",
                            messageLog.getInfoCount()));
        }
        ConsoleFormatter.newline();
    }

    private final void logMessage(final Path path, final Message message) {
        this.appMessages.add(path.toString(), message);
    }
}
