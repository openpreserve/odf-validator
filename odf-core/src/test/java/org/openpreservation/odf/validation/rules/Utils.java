package org.openpreservation.odf.validation.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.document.Documents;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;

public class Utils {
    private static final PackageParser parser = OdfPackages.getPackageParser();

    static OpenDocument getDocument(final URL resource) throws URISyntaxException, FileNotFoundException, ParseException {
        Path path = Paths.get(new File(resource.toURI()).getAbsolutePath());
        return Documents.openDocumentOf(path, parser.parsePackage(path));
    }

    static MessageLog getMessages(final URL resource, Rule rule) throws URISyntaxException, FileNotFoundException, ParseException {
        return rule.check(getDocument(resource));
    }
}
