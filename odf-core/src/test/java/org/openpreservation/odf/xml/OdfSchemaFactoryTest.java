package org.openpreservation.odf.xml;

import static org.junit.Assert.assertThrows;

import java.util.EnumSet;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.openpreservation.odf.xml.OdfSchemaFactory.Version;

public class OdfSchemaFactoryTest {
    @Test
    public void testGetSchema() {
        OdfSchemaFactory factory = new OdfSchemaFactory();
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    factory.getSchema(Namespaces.OFFICE, null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    factory.getSchema(null, Version.ODF_13);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    factory.getSchema(null, null);
                });
    }

    @Test
    public void testGetSchemas() {
        OdfSchemaFactory factory = new OdfSchemaFactory();
        EnumSet<Namespaces> namespaces = EnumSet.of(Namespaces.OFFICE);
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    factory.getSchemas(namespaces, null);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    factory.getSchemas(null, Version.ODF_13);
                });
        assertThrows("NullPointerException expected",
                NullPointerException.class,
                () -> {
                    factory.getSchemas(null, null);
                });
    }

    @Test
    public void testMissingSchema() {
        OdfSchemaFactory factory = new OdfSchemaFactory();
        EnumSet<Namespaces> namespaces = EnumSet.of(Namespaces.ANIM);
        assertThrows("NoSuchElementException expected",
                NoSuchElementException.class,
                () -> {
                    factory.getSchemas(namespaces, Version.ODF_13);
                });
    }

    @Test
    public void testNoNamespaces() {
        OdfSchemaFactory factory = new OdfSchemaFactory();
        EnumSet<Namespaces> empty = EnumSet.noneOf(Namespaces.class);
        assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> {
                    factory.getSchemas(empty, Version.ODF_13);
                });
    }
}
