package org.openpreservation.odf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.MessageLogImpl;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.validation.Validator;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "odf-sheets", mixinStandardHelpOptions = true, version = "odf-sheets 0.1", description = "Validates Open Document Format spreadsheets.")
class OdfSheets implements Callable<Integer> {
    private static final MessageFactory FACTORY = Messages.getInstance();

    @Parameters(paramLabel = "FILE", arity = "1..*", description = "A list of Open Document Format spreadsheet file to validate.")
    private File[] toValidateFiles;
    public final MessageLog messages = new MessageLogImpl();

    @Override
    public Integer call() throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException, IOException {
        Integer retStatus = 0;
        Validator validator = new Validator();
        for (File file : this.toValidateFiles) {
            Path toValidate = file.toPath();
            ConsoleFormatter.colourise(FACTORY.getInfo("APP-1", file.toString(), "bold"));
            if (validator.isFile(toValidate)) {
                validator.validate(toValidate);
            }
            for (Message message : validator.messages.getMessages()) {
                ConsoleFormatter.colourise(message);
            }
            retStatus = results() > retStatus ? results() : retStatus;

        }
        return retStatus;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new OdfSheets()).execute(args);
        System.exit(exitCode);
    }

    private Integer results() {
        Integer retStatus = 0;
        if (this.messages.isEmpty()) {
            ConsoleFormatter.info(FACTORY.getInfo("APP-2").getMessage());
        }
        for (Message message : this.messages.getMessages()) {
            ConsoleFormatter.colourise(message);
            if (message.isError() || message.isFatal()) {
                retStatus = 1;
            }
        }
        if (this.messages.hasErrors()) {
            ConsoleFormatter.error(String.format("NOT VALID, %d errors and %d warnings found.",
                    this.messages.getErrors().size(), this.messages.getWarnings().size()));
        } else {
            if (this.messages.hasWarnings()) {
                ConsoleFormatter.warn(String.format("VALID, no errors and %d warnings found.",
                        this.messages.getWarnings().size()));
            } else {
                ConsoleFormatter.info("VALID, no errors and no warnings found.");
            }
        }
        ConsoleFormatter.newline();
        return retStatus;
    }
}
