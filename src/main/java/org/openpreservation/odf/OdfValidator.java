package org.openpreservation.odf;

import java.io.File;
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
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "odf-validator", mixinStandardHelpOptions = true, version = "odf-validator 0.1", description = "Validates Open Document Format spreadsheets.")
class OdfValidator implements Callable<Integer> {
    private static final MessageFactory FACTORY = Messages.getInstance();

    @Parameters(paramLabel = "FILE", arity = "1..*", description = "A list of Open Document Format spreadsheet files to validate.")
    private File[] toValidateFiles;

    @Override
    public Integer call()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException, IOException {
        Validator validator = new Validator();
        final Map<Path, ValidationReport> reports = new HashMap<>();
        for (File file : this.toValidateFiles) {
            Path toValidate = file.toPath();
            ConsoleFormatter.colourise(FACTORY.getInfo("APP-1", file.toString(), "bold"));
            ValidationReport report = validator.validate(toValidate);
            reports.put(toValidate, report);
        }
        return results(reports);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new OdfValidator()).execute(args);
        System.exit(exitCode);
    }

    private Integer results (final Map<Path, ValidationReport> reports) {
        Integer retStatus = 0;
        for (Map.Entry<Path, ValidationReport> entry : reports.entrySet()) {
            retStatus = Math.max(retStatus, results(entry.getKey(), entry.getValue()));
        }
        return retStatus;
    }

    private Integer results(final Path path, final ValidationReport report) {
        Integer retStatus = 0;
        ConsoleFormatter.colourise(FACTORY.getInfo("APP-3", path.toString(), "bold"));
        if (report.getMessages().isEmpty()) {
            ConsoleFormatter.info(FACTORY.getInfo("APP-2").getMessage());
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
            packageWarnings += entry.getValue().getErrors().size();
            packageInfos += entry.getValue().getInfos().size();
        }
        if (packageErrors > 0) {
            ConsoleFormatter.error(String.format("NOT VALID, %d errors, %d warnings and %d info messages.",
                    packageErrors, packageWarnings, packageInfos));
        } else if (packageWarnings > 0) {
                ConsoleFormatter.warn(String.format("VALID, no errors, %d warnings found and %d info messages.",
                        packageWarnings, packageInfos));
        } else {
                ConsoleFormatter.info(String.format("VALID, no errors, no warnings and %d info message found.", packageInfos));
        }
        ConsoleFormatter.newline();
        return retStatus;
    }
}
