package org.openpreservation.odf.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.fmt.MimeTypes;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackageImpl;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.xml.XmlChecker;
import org.openpreservation.odf.xml.XmlParseResult;
import org.xml.sax.SAXException;

public class Validator {
    private static final String TAG_DOC = "office:document";
    private static final MessageFactory FACTORY = Messages.getInstance();

    public Validator() {
        super();
    }

    public ValidationReport validate(final Path toValidate)
            throws ParserConfigurationException, IOException, SAXException {
        if (toValidate == null) {
            throw new IllegalArgumentException("The supplied path is null");
        }
        if (!isFile(toValidate)) {
            throw new FileNotFoundException("Path parameter is not a file: " + toValidate);
        }
        if (OdfPackages.isPackage(toValidate)) {
            OdfPackage pkg = new OdfPackageImpl(toValidate);
            return pkg.validate();
        }
        ValidationReport report = new ValidationReport(toValidate);
        XmlChecker checker = new XmlChecker();
        XmlParseResult result = checker.parse(toValidate);
        if (result.isWellFormed) {
            if (result.isRootName(TAG_DOC)) {
                report.add(toValidate, FACTORY.getInfo("DOC-2", result.version));
                if (MimeTypes.isDocument(result.mimeType)) {
                    report.add(toValidate, FACTORY.getInfo("DOC-3", "Document", result.mimeType));
                } else if (MimeTypes.isTemplate(result.mimeType)) {
                    report.add(toValidate, FACTORY.getInfo("DOC-3", "Template", result.mimeType));
                } else {
                    report.add(toValidate, FACTORY.getError("DOC-4", result.mimeType));
                }
            }
            result = checker.validate(toValidate);
            report.add(toValidate, result.messages);
        } else {
            report.add(toValidate, result.messages);
        }
        return report;
    }

    private static final boolean isFile(final Path toCheck) {
        // Check that the supplied path is an existing, regular file
        return (toCheck != null && Files.exists(toCheck) && Files.isRegularFile(toCheck));
    }
}