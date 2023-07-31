package org.openpreservation.odf.pkg;

import java.util.Set;

public interface Manifest {
    /**
     * Get the declared version of the Manifest from the root element.
     * @return
     */
    public String getVersion();

    /**
     * Get the declared media type of the Manifest from the root "/" element.
     * @return the declared media type of the Manifest.
     */
    public String getRootMediaType();

    /**
     * Get the declared version of the ODF package from the root "/" element.
     * @return the declared version of the ODF package.
     */
    public String getRootVersion();

    /**
     * Get the number of entries in the Manifest.
     * @return the numbner of manifest entries.
     */
    public int getEntryCount();

    /**
     * Get the Set of FileEntry objects in the Manifest.
     * @return the set of file entries.
     */
    public Set<FileEntry> getEntries();

    /**
     * Get the Set of FileEntry objects in the Manifest that have the supplied media type.
     * @param mediaType the media type to filter the entries by.
     * @return the Set of FileEntry objects that have the supplied media type.
     */
    public Set<FileEntry> getEntriesByMediaType(final String mediaType);

    /**
     * Get a manifest entry by name
     * @param entryName the name to find the entry by.
     * @return the FileEntry object for the supplied name, or null if not found.
     */
    public FileEntry getEntry(final String entryName);

    /**
     * Get a manifest entry media type by name
     * @param entryName the name to find the entry by.
     * @return the FileEntry media type for the supplied name, or null if not found.
     */
    public String getEntryMediaType(final String entryName);
}
