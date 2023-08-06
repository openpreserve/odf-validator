package org.openpreservation.odf.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.openpreservation.odf.fmt.Formats;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.validation.ValidationReport;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class PropertyExtractorImpl implements PropertyExtractor {
    private static final List<Property> SUPPORTED_PROPERTIES = SUPPORTED_PROPERTIES();
    private static final String SAX_FEATURE_PREFIX = "http://xml.org/sax/features/";
    private static final String SAX_FEATURE_EXT_GEN_ENTITIES = SAX_FEATURE_PREFIX + "external-general-entities";
    private static final String SAX_FEATURE_EXT_PARAM_ENTITIES = SAX_FEATURE_PREFIX + "external-parameter-entities";
    final SAXParserFactory factory = getNonValidatingFactory();

    public PropertyExtractorImpl() throws ParserConfigurationException, SAXException {
        super();
    }

    @Override
    public List<Property> extractProperties(final OdfPackage odfPackage, final ValidationReport report)
            throws IOException {
        final List<Property> properties = new ArrayList<>();
        if (!report.isValid() || !"1.3".equals(odfPackage.getManifest().getVersion())) {
            properties.add(PropertyImpl.create("Standard Compliance", "ODS-2"));
        }
        if (!odfPackage.hasMimeEntry()) {
            properties.add(PropertyImpl.create("MIME type Entry", "ODS-3"));
        }
        final String ext = odfPackage.getName().substring(odfPackage.getName().lastIndexOf('.') + 1);
        if (!Formats.fromExtension(ext).equals(Formats.fromMime(odfPackage.getMimeType()))) {
            properties.add(PropertyImpl.create("Extension & MIME type", "ODS-4"));
        }
        properties.addAll(detectFeatures(odfPackage));
        return properties;
    }

    @Override
    public List<Property> supportedProperties() {
        return SUPPORTED_PROPERTIES;
    }

    private final List<Property> detectFeatures(final OdfPackage odfPackage) throws IOException {
        final List<Property> properties = new ArrayList<>();
        for (final String path : odfPackage.getXmlEntryPaths()) {
            if (!path.endsWith(OdfPackages.NAME_META)) {
                properties.add(PropertyImpl.create("Metadata", "ODS-11"));
            }
            try (final InputStream is = odfPackage.getEntryXmlStream(path)) {
                final SAXParser saxParser = factory.newSAXParser();
                final XMLReader reader = saxParser.getXMLReader();
                final FeatureHandler handler = new FeatureHandler();
                reader.setContentHandler(handler);
                reader.parse(new InputSource(is));
                properties.addAll(handler.properties);
            } catch (final SAXException | ParserConfigurationException e) {
                throw new IOException(e);
            }
        }
        return properties;
    }

    private static final List<Property> SUPPORTED_PROPERTIES() {
        final List<Property> properties = new ArrayList<>();
        properties.add(PropertyImpl.create("Password Protection", "ODS-1"));
        properties.add(PropertyImpl.create("Standard Compliance", "ODS-2"));
        properties.add(PropertyImpl.create("MIME type Entry", "ODS-3"));
        properties.add(PropertyImpl.create("Extension & MIME type", "ODS-4"));
        properties.add(PropertyImpl.create("External Data", "ODS-5"));
        properties.add(PropertyImpl.create("Embedded Objects", "ODS-6"));
        properties.add(PropertyImpl.create("Content", "ODS-7"));
        properties.add(PropertyImpl.create("Macros", "ODS-8"));
        properties.add(PropertyImpl.create("Fonts", "ODS-9"));
        properties.add(PropertyImpl.create("Settings File", "ODS-10"));
        properties.add(PropertyImpl.create("Metadata", "ODS-11"));
        properties.add(PropertyImpl.create("Cell Hyperlinks", "ODS-12"));
        properties.add(PropertyImpl.create("Preview Image", "ODS-13"));
        return properties;
    }

    private static final class FeatureHandler extends DefaultHandler {
        public List<Property> properties = new ArrayList<>();

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            properties.clear();
        }

        @Override
        public void startElement(final String uri, final String localName, final String qName,
                final Attributes attributes)
                throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if ("office:scripts".equals(qName)) {
                properties.add(PropertyImpl.create("Macros", "ODS-8"));
            }
            if ("draw:object-ole".equals(qName)) {
                properties.add(PropertyImpl.create("Embedded Objects", "ODS-6"));
            }
            if (attributes.getValue("xlink:href") != null && !attributes.getValue("xlink:href").startsWith("./")
                    && !attributes.getValue("xlink:href").startsWith("#")) {
                properties.add(PropertyImpl.create("External Data", "ODS-5"));
            }
        }
    }

    public static final SAXParserFactory getNonValidatingFactory()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance(
                "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", ClassLoader.getSystemClassLoader());
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        factory.setFeature(SAX_FEATURE_EXT_GEN_ENTITIES, false);
        factory.setFeature(SAX_FEATURE_EXT_PARAM_ENTITIES, false);
        return factory;
    }

}
