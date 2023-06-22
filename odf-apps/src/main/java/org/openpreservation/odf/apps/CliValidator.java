package org.openpreservation.odf.apps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.Validator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "odf-validator", mixinStandardHelpOptions = true, version = "odf-validator 0.1", description = "Validates Open Document Format spreadsheets.")
class CliValidator implements Callable<Integer> {
    private static final MessageFactory FACTORY = Messages
            .getInstance("org.openpreservation.odf.apps.messages.Messages");

    @Parameters(paramLabel = "FILE", arity = "1..*", description = "A list of Open Document Format spreadsheet files to validate.")
    private File[] toValidateFiles;
    private final Validator validator = new Validator();
    private final Map<Path, ValidationReport> validationReports = new HashMap<>();
    private final Map<Path, MessageLog> appMessages = new HashMap<>();

    @Override
    public Integer call()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException, IOException {
        for (File file : this.toValidateFiles) {
            Path toValidate = file.toPath();
            ConsoleFormatter.colourise(FACTORY.getInfo("APP-1", toValidate.toString()));
            validationReports.put(toValidate, validatePath(toValidate));
        }
        return results();
    }

    private ValidationReport validatePath(final Path toValidate) {
        try {
            return validator.validate(toValidate);
        } catch (FileNotFoundException e) {
            this.logMessage(toValidate, FACTORY.getError("APP-2", toValidate.toString()));
        } catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
                for (Message message : this.appMessages.get(entry.getKey()).getMessages()) {
                    ConsoleFormatter.colourise(message);
                    if (message.isError() || message.isFatal()) {
                        retStatus = 1;
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
        int packageErrors = 0;
        int packageWarnings = 0;
        int packageInfos = 0;
        for (Map.Entry<Path, MessageLog> entry : report.documentMessages.entrySet()) {
            for (Message message : entry.getValue().getMessages()) {
                ConsoleFormatter.colourise(entry.getKey(), message);
                if (message.isError() || message.isFatal()) {
                    retStatus = 1;
                }
            }
            packageErrors += entry.getValue().getErrors().size();
            packageWarnings += entry.getValue().getWarnings().size();
            packageInfos += entry.getValue().getInfos().size();
        }
        if (packageErrors > 0) {
            ConsoleFormatter.error(String.format("NOT VALID, %d errors, %d warnings and %d info messages.",
                    packageErrors, packageWarnings, packageInfos));
        } else if (packageWarnings > 0) {
            ConsoleFormatter.warn(String.format("VALID, no errors, %d warnings found and %d info messages.",
                    packageWarnings, packageInfos));
        } else {
            ConsoleFormatter
                    .info(String.format("VALID, no errors, no warnings and %d info message found.", packageInfos));
        }
        ConsoleFormatter.newline();
        return retStatus;
    }

    private final void logMessage(final Path path, final Message message) {
        this.appMessages.putIfAbsent(path, Messages.messageLogInstance());
        this.appMessages.get(path).add(message);
    }
}
