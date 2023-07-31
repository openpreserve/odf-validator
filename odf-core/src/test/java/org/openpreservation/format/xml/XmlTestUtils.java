package org.openpreservation.format.xml;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class XmlTestUtils {
    public static URL exampleUrl = getExampleUrl();
    public static URI exampleUri = URI.create("urn:example:namespace");
    public static URI exampleUriUrl = URI.create("http://www.example.com/namespace/");
    public static Namespace exampleNamespace = NamespaceImpl.of(exampleUri, "example", exampleUrl);

    private static URL getExampleUrl() {
        try {
            return new URL("http://www.example.com/namespace/schema.xsd");
        } catch (MalformedURLException e) {
            throw new AssertionError("Test mangled.");
        }
    }

}
