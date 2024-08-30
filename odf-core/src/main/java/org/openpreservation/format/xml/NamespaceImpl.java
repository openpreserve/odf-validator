package org.openpreservation.format.xml;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import org.openpreservation.utils.Checks;

final class NamespaceImpl implements Namespace {
    private static final String STRING = "String";

    static NamespaceImpl of(final URI id, final String prefix, final URL schemalocation) {
        Objects.requireNonNull(id, String.format(Checks.NOT_NULL, "id", "URI"));
        Objects.requireNonNull(prefix, String.format(Checks.NOT_NULL, "prefix", STRING));
        try {
            return new NamespaceImpl(id, prefix, (schemalocation != null) ? schemalocation.toURI() : null);
        } catch (final URISyntaxException e) {
            throw new IllegalArgumentException("Parameter id MUST be a legal URI.", e);
        }
    }

    static NamespaceImpl of(final String id, final String prefix) {
        Objects.requireNonNull(id, String.format(Checks.NOT_NULL, "id", STRING));
        Objects.requireNonNull(prefix, String.format(Checks.NOT_NULL, "prefix", STRING));
        return NamespaceImpl.of(URI.create(id), prefix, null);
    }

    private final URI id;

    private final String prefix;

    private final URI schemalocation;

    private NamespaceImpl(final URI id, final String prefix, final URI schemalocation) {
        super();
        this.id = id;
        this.prefix = prefix;
        this.schemalocation = schemalocation;
    }

    @Override
    public URI getId() {
        return this.id;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public URL getSchemalocation() {
        try {
            return (this.schemalocation != null) ? this.schemalocation.toURL() : null;
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException("Parameter schemalocation MUST be a legal URL.", e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
        result = prime * result + ((schemalocation == null) ? 0 : schemalocation.hashCode());
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
        final NamespaceImpl other = (NamespaceImpl) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (prefix == null) {
            if (other.prefix != null)
                return false;
        } else if (!prefix.equals(other.prefix))
            return false;
        if (schemalocation == null) {
            if (other.schemalocation != null)
                return false;
        } else if (!schemalocation.equals(other.schemalocation))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "NamespaceImpl [id=" + id + ", prefix=" + prefix + ", schemalocation=" + schemalocation + "]";
    }
}
