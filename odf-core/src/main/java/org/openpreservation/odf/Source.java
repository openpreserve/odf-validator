package org.openpreservation.odf;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.fmt.FormatSniffer;
import org.openpreservation.odf.fmt.Formats;

/**
 * Represents a source file or stream to be processed.
 */
public class Source {
    public final Path path;
    public final Formats detectedFormat;

    private Source(Path path) throws IOException {
        this.path = path;
        this.detectedFormat = FormatSniffer.sniff(path);
    }

    /**
     * Create a new source from a Path
     *
     * @param path the path to open as a source
     * @return a new Source instance created from the path
     * @throws IOException when there is an issue reading the path
     */
    public static Source from(Path path) throws IOException {
        return new Source(path);
    }

    /**
     * Uses the {@link FormatSniffer} to see if the supplied path resolves to a ZIP
     * instance.
     * 
     * This check only tests the file header bytes and doesn't parse
     * the resolved path to ensure the zip is valid. See
     * {@link Source#isValidZip(Path)} for that.
     *
     * @param toCheck a <code>Path<code> to sniff as a zip file
     * @return <code>true</code> if the supplied path has a zip signature,
     *                <code>false</code> otherwise.
     * @throws IOException if there's an issue reading the resolved
     *                     <code>Path</code>
     */
    public static final boolean isZip(final Path toCheck) throws IOException {
        return FormatSniffer.sniff(toCheck, Formats.ZIP.getMaxSignatureLength()) == Formats.ZIP;
    }

    /**
     * Check if the supplied <code>Path</code> resolves to a valid zip archive
     * instance.
     * 
     * This is a full library parse of the resolved <code>Path<code>.
     *
     * @param toCheck a <code>Path</code> to resolve and test
     * @return <code>true</code> if the supplied path resolves to a valid zip file,
     *         else <code>false</code>.
     * @throws IOException if there's an issue reading the resolved <code>Path<code>
     */
    public static final boolean isValidZip(final Path toCheck) throws IOException {
        try (ZipFile zipFile = new ZipFile(toCheck.toFile())) {
            return true;
        } catch (final ZipException e) {
            /**
             * No need to report this as an error as it simply means that the file is not a
             * ZIP
             */
        }
        return false;
    }

    /**
     * Determines whether the supplied path is an XML document or not.
     *
     * @param toCheck a Java Path for the file to check
     * @return true if the supplied path is an XML document, false otherwise.
     * @throws IOException when there is an error reading the file.
     */
    public static final boolean isXml(final Path toCheck) throws IOException {
        return FormatSniffer.sniff(toCheck, Formats.XML.getMaxSignatureLength()) == Formats.XML;
    }

    public OpenDocument getOpenDocument() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
