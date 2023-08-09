package org.openpreservation.odf.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.openpreservation.format.xml.XmlParser;
import org.openpreservation.utils.Checks;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

final class MetadataImpl implements Metadata {
    private static final class MetadataHandler extends DefaultHandler {
        private String version = "";
        private boolean inMeta = false;
        private String currentElement = "";
        private final Map<String, String> stringValues = new HashMap<>();
        private final List<UserDefinedField> userDefinedFields = new ArrayList<>();

        public MetadataImpl getMetadata() {
            return MetadataImpl.of(this.version, this.stringValues, this.userDefinedFields);
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            this.stringValues.clear();
            this.userDefinedFields.clear();
            this.version = "";
            this.currentElement = "";
            this.inMeta = false;
        }

        @Override
        public void startElement(final String uri, final String localName, final String qName,
                final Attributes attributes)
                throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if ("office:document".equals(qName) || "office:document-meta".equals(qName)) {
                if (attributes.getValue("office:version") != null) {
                    this.version = attributes.getValue("office:version");
                }
            } else if ("office:meta".equals(qName)) {
                this.inMeta = true;
            } else if (this.inMeta) {
                this.currentElement = qName;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (this.inMeta) {
                String value = new String(ch, start, length);
                if (currentElement != null && !currentElement.trim().isEmpty() && !value.trim().isEmpty()) {
                    this.stringValues.put(this.currentElement, value);
                }
            }
        }

        @Override
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if ("office:meta".equals(qName)) {
                this.inMeta = false;
            }
        }
    }

    public static final class UserDefinedFieldImpl implements UserDefinedField {
        static final UserDefinedField of(final String name, final String valueType, final String value) {
            Objects.requireNonNull(name, String.format(Checks.NOT_NULL, "name", "String"));
            return new UserDefinedFieldImpl(name, valueType, value);
        }

        private final String name;
        private final String valueType;

        private final String value;

        private UserDefinedFieldImpl(final String name, final String valueType, final String value) {
            super();
            this.name = name;
            this.valueType = valueType;
            this.value = value;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getValueType() {
            return this.valueType;
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((valueType == null) ? 0 : valueType.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final UserDefinedFieldImpl other = (UserDefinedFieldImpl) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (valueType == null) {
                if (other.valueType != null)
                    return false;
            } else if (!valueType.equals(other.valueType))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "UserDefinedFieldImpl [name=" + name + ", valueType=" + valueType + ", value=" + value + "]";
        }
    }

    static final MetadataImpl of(final String version, final Map<String, String> stringValues,
            final List<UserDefinedField> userDefinedFields) {
        Objects.requireNonNull(stringValues, String.format(Checks.NOT_NULL, "stringValues", "Map<String, String>"));
        Objects.requireNonNull(userDefinedFields,
                String.format(Checks.NOT_NULL, "userDefinedFields", "List<UserDefinedField>"));
        return new MetadataImpl(version, stringValues, userDefinedFields);
    }

    static final MetadataImpl from(final InputStream metaStream)
            throws ParserConfigurationException, SAXException, IOException {
        Objects.requireNonNull(metaStream, String.format(Checks.NOT_NULL, "metaStream", "InputStream"));
        final SAXParserFactory nonValidatingFactory = XmlParser.getNonValidatingFactory();
        final SAXParser parser = nonValidatingFactory.newSAXParser();
        final XMLReader reader = parser.getXMLReader();
        final MetadataHandler handler = new MetadataHandler();
        reader.setContentHandler(handler);
        reader.parse(new InputSource(metaStream));
        return handler.getMetadata();
    }

    private final String version;

    private final Map<String, String> stringValues;

    private final List<UserDefinedField> userDefinedFields;

    private MetadataImpl(final String version, final Map<String, String> stringValues,
            final List<UserDefinedField> userDefinedFields) {
        super();
        this.version = version;
        this.stringValues = Collections.unmodifiableMap(stringValues);
        this.userDefinedFields = Collections.unmodifiableList(userDefinedFields);
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public Map<String, String> getStringValueMap() {
        return Collections.unmodifiableMap(this.stringValues);
    }

    @Override
    public String getStringValue(final String qualifiedName) {
        return this.stringValues.get(qualifiedName);
    }

    @Override
    public String getStringValue(final String prefix, final String localName) {
        return this.stringValues.get(prefix + ":" + localName);
    }

    @Override
    public List<UserDefinedField> getUserDefinedFields() {
        return Collections.unmodifiableList(this.userDefinedFields);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((stringValues == null) ? 0 : stringValues.hashCode());
        result = prime * result + ((userDefinedFields == null) ? 0 : userDefinedFields.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MetadataImpl other = (MetadataImpl) obj;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        if (stringValues == null) {
            if (other.stringValues != null)
                return false;
        } else if (!stringValues.equals(other.stringValues))
            return false;
        if (userDefinedFields == null) {
            if (other.userDefinedFields != null)
                return false;
        } else if (!userDefinedFields.equals(other.userDefinedFields))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MetadataImpl [version=" + version + ", stringValues=" + stringValues + ", userDefinedFields="
                + userDefinedFields + "]";
    }
}
