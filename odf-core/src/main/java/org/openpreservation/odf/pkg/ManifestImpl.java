package org.openpreservation.odf.pkg;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

final class ManifestImpl implements Manifest {
    private static final class ManifestHandler extends DefaultHandler {
        private String version = "";

        private final Map<String, FileEntry> entries = new HashMap<>();

        public ManifestImpl getManifest() {
            return ManifestImpl.of(this.version, this.entries);
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            this.entries.clear();
            this.version = "";
        }

        @Override
        public void startElement(final String uri, final String localName, final String qName,
                final Attributes attributes)
                throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if ("manifest:file-entry".equals(qName)) {
                final String fullPath = attributes.getValue("manifest:full-path");
                final String mediaType = attributes.getValue("manifest:media-type");
                final String version = attributes.getValue("manifest:version");
                long size = -1;
                try {
                    size = Long.parseLong(attributes.getValue("manifest:size"));
                } catch (final NumberFormatException e) {
                    // ignore
                }
                this.entries.put(fullPath, FileEntryImpl.of(fullPath, mediaType, size, version));
            } else if ("manifest:manifest".equals(qName)) {
                this.version = attributes.getValue("manifest:version");
            }
        }
    }

    static final ManifestImpl of(final String version, final Map<String, FileEntry> entries) {
        Objects.requireNonNull(entries, String.format(Checks.NOT_NULL, "entries", "Map<String, FileEntry>"));
        return new ManifestImpl(version, entries);
    }

    static final ManifestImpl from(final InputStream manifestStream)
            throws ParserConfigurationException, SAXException, IOException {
        Objects.requireNonNull(manifestStream, String.format(Checks.NOT_NULL, "manifestStream", "InputStream"));
        final SAXParserFactory nonValidatingFactory = XmlParser.getNonValidatingFactory();
        final SAXParser parser = nonValidatingFactory.newSAXParser();
        final XMLReader reader = parser.getXMLReader();
        final ManifestHandler handler = new ManifestHandler();
        reader.setContentHandler(handler);
        reader.parse(new InputSource(manifestStream));
        return handler.getManifest();
    }

    private final String version;

    private final Map<String, FileEntry> entries;

    private final boolean hasRoot;

    private ManifestImpl(final String version, final Map<String, FileEntry> entries) {
        this.version = version;
        this.entries = entries;
        this.hasRoot = entries.containsKey("/");
    }

    @Override
    public String getVersion() {
        return (this.version == null || this.version.isEmpty()) ? this.getRootVersion() : this.version;
    }

    @Override
    public String getRootMediaType() {
        return this.hasRoot ? this.getEntry("/").getMediaType() : null;
    }

    @Override
    public String getRootVersion() {
        return this.hasRoot ? this.getEntry("/").getVersion() : null;
    }

    @Override
    public int getEntryCount() {
        return this.entries.size();
    }

    @Override
    public Set<FileEntry> getEntries() {
        return this.entries.values().stream().collect(Collectors.toSet());
    }

    @Override
    public Set<FileEntry> getEntriesByMediaType(final String mediaType) {
        return this.entries.values().stream().filter(e -> e.getMediaType().equals(mediaType))
                .collect(Collectors.toSet());
    }

    @Override
    public FileEntry getEntry(final String entryName) {
        return this.entries.get(entryName);
    }

    @Override
    public String getEntryMediaType(final String entryName) {
        final FileEntry entry = this.entries.get(entryName);
        return (entry != null) ? this.entries.get(entryName).getMediaType() : null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((entries == null) ? 0 : entries.hashCode());
        result = prime * result + (hasRoot ? 1231 : 1237);
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
        final ManifestImpl other = (ManifestImpl) obj;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        if (entries == null) {
            if (other.entries != null)
                return false;
        } else if (!entries.equals(other.entries))
            return false;
        if (hasRoot != other.hasRoot)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ManifestImpl [version=" + version + ", entries=" + entries + ", hasRoot=" + hasRoot + "]";
    }
}
