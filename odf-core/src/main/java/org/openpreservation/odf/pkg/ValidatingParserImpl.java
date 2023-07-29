package org.openpreservation.odf.pkg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.MessageFactory;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.validation.ValidationReport;
import org.xml.sax.SAXException;

public class ValidatingParserImpl implements ValidatingParser {
    private static final MessageFactory FACTORY = Messages.getInstance();

    private ValidationReport report;

    private ValidatingParserImpl() {
        super();
    }

    @Override
    public ValidationReport validatePackage(final Path toValidate)
            throws IOException, ParserConfigurationException, SAXException {
        return validate(this.parsePackage(toValidate));
    }

    @Override
    public ValidationReport validatePackage(final File toValidate)
        throws IOException, ParserConfigurationException, SAXException {
        return validate(this.parsePackage(toValidate));
    }

    @Override
    public ValidationReport validatePackage(final InputStream toValidate, final String path)
            throws IOException, ParserConfigurationException, SAXException {
        return validate(this.parsePackage(toValidate));
    }

    @Override
    public OdfPackage parsePackage(final Path toParse) throws IOException, ParserConfigurationException, SAXException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parsePackage'");
    }

    @Override
    public OdfPackage parsePackage(final File toParse) throws IOException, ParserConfigurationException, SAXException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parsePackage'");
    }

    @Override
    public OdfPackage parsePackage(final InputStream toParse) throws IOException, ParserConfigurationException, SAXException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parsePackage'");
    }

    static final ValidatingParserImpl getInstance() {
        return new ValidatingParserImpl();
    }
 
    private ValidationReport validate(final OdfPackage toValidate) {
        if (!toValidate.hasMimeEntry()) {
            report.add(Constants.MIMETYPE, FACTORY.getWarning("PKG-2"));
        }
        if (!toValidate.hasManifest()) {
            report.add(Constants.PATH_MANIFEST, FACTORY.getError("PKG-4"));
        }
        boolean isFirst = true;
        // TODO: implement this
        // for (final ZipEntry entry : toValidate.getZipEntries()) {
        //     if ((entry.getMethod() != ZipEntry.STORED) && (entry.getMethod() != ZipEntry.DEFLATED)) {
        //         // Entries SHALL be uncompressesed (Stored) or use deflate compression
        //         report.add(entry.getName(), FACTORY.getError("PKG-1", entry.getName()));
        //     }
        //     if (entry.getName().equals(Constants.MIMETYPE) && !entry.isDirectory()) {
        //         // Check the mimetype entry when it's found
        //         this.validateMimeEntry(entry, isFirst);
        //     }
        //     if (entry.getName().startsWith(Constants.NAME_META_INF)) {
        //         if (entry.isDirectory() && !Constants.NAME_META_INF.equals(entry.getName())) {
        //             report.add(entry.getName(), FACTORY.getError("PKG-3", entry.getName()));
        //         }
        //     }
        //     isFirst = false;
        // }
        return this.report;
    }

    private void validateMimeEntry(final ZipEntry mimeEntry, final boolean isFirst) {
        if (!isFirst) {
            report.add(mimeEntry.getName(), FACTORY.getError("PKG-7"));
        }
        if (mimeEntry.getMethod() != ZipEntry.STORED) {
            report.add(mimeEntry.getName(), FACTORY.getError("PKG-6"));
        }
        if (mimeEntry.getExtra() != null) {
            report.add(mimeEntry.getName(), FACTORY.getError("PKG-8"));
        }
    }
}
