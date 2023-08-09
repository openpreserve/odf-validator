package org.openpreservation.odf.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.xml.ParseResult;
import org.xml.sax.SAXException;

public final class OdfXmlDocuments {
    private OdfXmlDocuments() {
        throw new AssertionError("org.openpreservation.odf.xml.OdfDocuments shoudl not be instantiated");
    }

    /**
     * Instantiates an OdfXmlDocument from an XML ParseResult
     *
     * @param parseResult a org.openpreservation.format.xml.ParseResult from parsing
     *                    an ODF XML document.
     * @return an OdfXmlDocument instance created from the passed ParseResult
     */
    public static final OdfXmlDocument odfXmlDocumentOf(final ParseResult parseResult) {
        return OdfXmlDocumentImpl.of(parseResult);
    }

    /**
     * Instatiates an OdfXmlDocument from an InputStream.
     *
     * @param docStream an InputStream to an ODF XML document.
     * @return an OdfXmlDocument instance created from the passed InputStream.
     * @throws IOException                  When there is an error reading the
     *                                      InputStream.
     * @throws ParserConfigurationException When the XML parser cannot be
     *                                      configured, probably a system setup
     *                                      issue.
     * @throws SAXException                 When an exception occurs during SAX
     *                                      processing, likely a system setup issue.
     */
    public static final OdfXmlDocument xmlDocumentFrom(final InputStream docStream)
            throws IOException, ParserConfigurationException, SAXException {
        return OdfXmlDocumentImpl.from(docStream);
    }

    /**
     * Instantiates a Metadata object from an InputStream.
     *
     * @param metaStream and InputStream to an ODF XML document thought to contain a
     *                   metadata element.
     * @return a Metadata object created from the passed InputStream.
     * @throws IOException                  When there is an error reading the
     *                                      InputStream.
     * @throws ParserConfigurationException When the XML parser cannot be
     *                                      configured, probably a system setup
     *                                      issue.
     * @throws SAXException                 When an exception occurs during SAX
     *                                      processing, likely a system setup issue.
     */
    public static final Metadata metadataFrom(final InputStream metaStream)
            throws ParserConfigurationException, SAXException, IOException {
        return MetadataImpl.from(metaStream);
    }

}
