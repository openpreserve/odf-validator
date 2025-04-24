package org.openpreservation.odf.document;

import static org.junit.Assert.assertThrows;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.xml.sax.SAXException;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OdfDocumentTest {
    private Utils utils = new Utils();
    public OdfDocumentTest() throws IOException, ParserConfigurationException, SAXException {
    }

    @Test
    public void testInstantiation() {
        OdfXmlDocument nullDoc = null;
        ParseResult nullResult = null;
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(nullDoc, utils.metadata);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(utils.odfDocument, null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(nullResult, utils.metadata);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    OdfDocumentImpl.of(utils.parseResult, null);
                });
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(OdfDocumentImpl.class).verify();
    }
}
