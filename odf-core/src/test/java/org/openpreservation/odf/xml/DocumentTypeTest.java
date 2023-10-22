package org.openpreservation.odf.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.openpreservation.odf.fmt.Formats;

public class DocumentTypeTest {
    @Test
    public void testGetTypeByNullMimeString() {
        assertThrows("NullPointerException expected",
        NullPointerException.class,
        () -> {
            DocumentType.getTypeByMimeString(null);
        });
    }

    @Test
    public void testGetTypeByMimeString() {
        DocumentType dt = DocumentType.getTypeByMimeString("application/vnd.oasis.opendocument.spreadsheet");
        assertEquals("Error with retrieved DocumentType", DocumentType.SPREADSHEET, dt);
        dt = DocumentType.getTypeByMimeString("application/octet-stream");
        assertNull("Error with retrieved DocumentType", dt);
    }

    @Test
    public void testGetTypeByNullFormat() {
        assertThrows("NullPointerException expected",
        NullPointerException.class,
        () -> {
            DocumentType.getTypeByFormat(null);
        });
    }

    @Test
    public void testGetTypeByFormat() {
        DocumentType dt = DocumentType.getTypeByFormat(Formats.ODS);
        assertEquals("Error with retrieved DocumentType", DocumentType.SPREADSHEET, dt);
        dt = DocumentType.getTypeByFormat(Formats.UNKNOWN);
        assertNull("Error with retrieved DocumentType", dt);
    }

    @Test
    public void testGetTypeByNullBodyElement() {
        assertThrows("NullPointerException expected",
        NullPointerException.class,
        () -> {
            DocumentType.getTypeByBodyElement(null);
        });
    }

    @Test
    public void testGetTypeByBodyElement() {
        DocumentType dt = DocumentType.getTypeByBodyElement("spreadsheet");
        assertEquals("Error with retrieved DocumentType", DocumentType.SPREADSHEET, dt);
        dt = DocumentType.getTypeByBodyElement("office:spreadsheet");
        assertEquals("Error with retrieved DocumentType", DocumentType.SPREADSHEET, dt);
        assertEquals("Wrong body element name", "office:spreadsheet", dt.getBodyElementName());
        dt = DocumentType.getTypeByBodyElement("unknown");
        assertNull("Error with retrieved DocumentType", dt);
    }
}
