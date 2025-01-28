package org.openpreservation.odf.apps;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Profile;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.Validator;
import org.openpreservation.odf.validation.rules.Rules;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    @Parameters(paramLabel = "FILE", arity = "1..*", description = "A list of Open Document Format spreadsheet files to validate.")
    private File[] toValidateFiles;
    @Option(names = { "--format" }, description = "Output results as TEXT, JSON or XML.", defaultValue = "TEXT")
    private FormatOption format = FormatOption.TEXT;
    private final Validator validator = new Validator();
    private MessageLog appMessages = Messages.messageLogInstance();

    @Override
    public Integer call() throws JsonProcessingException {
        Integer retStatus = 0;
        for (File file : this.toValidateFiles) {
            Path toValidate = file.toPath();
            this.appMessages = Messages.messageLogInstance();
            ConsoleFormatter.colourise(FACTORY.getInfo("APP-1", toValidate.toString()));
            final ValidationReport validationResult = validatePath(toValidate);
            if (validationResult != null) {
                retStatus = Math.max(retStatus, outputValidationResult(toValidate, validationResult, this.format));
                if (this.profileFlag) {
                    final ValidationReport profileResult = profilePath(toValidate);
                    if (profileResult != null) {
                        retStatus = Math.max(retStatus, outputProfileResult(toValidate, profileResult, this.format));
                    }
                }
            }
            if (this.appMessages.hasErrors()) {
                retStatus = Math.max(retStatus,
                        processMessageLists(this.appMessages.getMessages().values()));
            }
        }
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

    private Integer processMessageLists(Collection<List<Message>> messageLists) {
        Integer status = 0;
        for (List<Message> messages : messageLists) {
            status = Math.max(status, processMessageList(messages));
        }
        return status;
    }

    private Integer processMessageList(final List<Message> messages) {
        Integer status = 0;
        for (Message message : messages) {
            ConsoleFormatter.colourise(message);
            if (message.isFatal()) {
                status = 1;
            }
        }
        return status;
    }

    private static Integer outputValidationResult(final Path path, final ValidationReport report, FormatOption format) throws JsonProcessingException {
        ConsoleFormatter.colourise(FACTORY.getInfo("APP-4", path.toString(), "bold"));
        if (report.getValidationResult().getMessages().isEmpty()) {
            ConsoleFormatter.info(FACTORY.getInfo("APP-3").getMessage());
        }
        Integer status = report.getValidationResult().getMessageLog().hasFatals() || report.getValidationResult().getMessageLog().hasErrors() ? 1 : 0;
        if (format == FormatOption.JSON)
            ouptutJson(report);
        else if (format == FormatOption.XML)
            ouptutXml(report);
        else
            outputMessageLog(report.getValidationResult().getMessageLog().getMessages());
        outputSummary(report.getValidationResult().isEncrypted(), report.getValidationResult().getMessageLog());
        return status;
    }

    private static Integer outputProfileResult(final Path path, final ValidationReport report, FormatOption format) throws JsonProcessingException {
        ConsoleFormatter.colourise(FACTORY.getInfo("APP-5", report.getProfileResult().getName(), path.toString(), "bold"));
        Integer status = report.getProfileResult().getMessageLog().hasFatals() || report.getProfileResult().getMessageLog().hasErrors() ? 1 : 0;
        if (report.getValidationResult() != null && report.getValidationResult().getMessageLog().hasErrors() || report
                .getValidationResult().getMessageLog().hasFatals()) {
            status = 1;
        }
        MessageLog profileMessages = (report.getValidationResult() != null)
                ? report.getValidationResult().getMessageLog()
                : Messages.messageLogInstance();
        // profileMessages.add(report.getProfileResult().getMessageLog().getMessages());
        if (format == FormatOption.JSON)
            ouptutJson(report);
        else if (format == FormatOption.XML)
            ouptutXml(report);
        else
            outputMessageLog(report.getProfileResult().getMessageLog().getMessages());
        outputSummary(report.getValidationResult().isEncrypted(), profileMessages);
        return status;
    }

    private static void outputMessageLog(final Map<String, List<Message>> messageMap) {
        for (Map.Entry<String, List<Message>> entry : messageMap.entrySet()) {
            outputMessage(entry.getKey(), entry.getValue());
        }
    }

    private static void outputMessage(final String path, final List<Message> messages) {
        for (Message message : messages) {
            ConsoleFormatter.colourise(Paths.get(path), message);
        }
    }

    private static void ouptutJson(final ValidationReport result) throws JsonProcessingException {
        var mapper = new ObjectMapper().registerModule(new JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT);
        ConsoleFormatter.info(mapper.writeValueAsString(result));
    }

    private static void ouptutXml(final ValidationReport result) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ConsoleFormatter.info(xmlMapper.writeValueAsString(result));
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

    static private enum FormatOption {
        JSON, XML, TEXT
    }
}
