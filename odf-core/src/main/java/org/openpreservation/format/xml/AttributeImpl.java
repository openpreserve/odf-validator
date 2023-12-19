package org.openpreservation.format.xml;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openpreservation.utils.Checks;
import org.xml.sax.Attributes;

final class AttributeImpl implements Attribute {
    static final Attribute of(final int index, final String qualifiedName, final String value, final URI uri,
            final String type) {
        Objects.requireNonNull(qualifiedName, String.format(Checks.NOT_NULL, "qualifiedName", "String"));
        Objects.requireNonNull(value, String.format(Checks.NOT_NULL, "value", "String"));
        if (qualifiedName.isBlank()) {
            throw new IllegalArgumentException(String.format(Checks.NOT_EMPTY, "qualifiedName"));
        }
        if (index < 0) {
            throw new IllegalArgumentException(String.format(Checks.GREATER_THAN_OR_EQUAL_TO, "index", "0"));
        }
        return new AttributeImpl(index, qualifiedName, value, uri, type);
    }
    static final List<Attribute> of(final Attributes attributes) {
        final List<Attribute> result = new ArrayList<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            result.add(of(i, attributes.getQName(i), attributes.getValue(i), URI.create(attributes.getURI(i)),
                    attributes.getType(i)));
        }
        return result;
    }
    private final int index;
    private final String qualifiedName;
    private final String value;

    private final URI uri;

    private final String type;

    private AttributeImpl(final int index, final String qualifiedName, final String value, final URI uri,
            final String type) {
        super();
        this.index = index;
        this.qualifiedName = qualifiedName;
        this.value = value;
        this.uri = uri;
        this.type = type;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public String getLocalName() {
        if (this.qualifiedName.indexOf(':') == -1)
            return this.qualifiedName;
        return this.qualifiedName.substring(this.qualifiedName.indexOf(':') + 1);
    }

    @Override
    public String getQualifiedName() {
        return this.qualifiedName;
    }

    @Override
    public String getPrefix() {
        if (this.qualifiedName.indexOf(':') == -1)
            return "";
        return this.qualifiedName.substring(0, this.qualifiedName.indexOf(':'));
    }

    @Override
    public URI getURI() {
        return this.uri;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        final AttributeImpl other = (AttributeImpl) obj;
        if (index != other.index)
            return false;
        if (qualifiedName == null) {
            if (other.qualifiedName != null)
                return false;
        } else if (!qualifiedName.equals(other.qualifiedName))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        if (uri == null) {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AttributeImpl [index=" + index + ", qualifiedName=" + qualifiedName + ", value=" + value + ", uri="
                + uri + ", type=" + type + "]";
    }
}
