# Detection of ODF Conditions

## ODF Specification Errors

### Document Errors



### Package Errors

| Ref | Detection |
|-----|-------------|
| PKG-1 | An OpenDocument package SHALL be a well formed Zip Archive.<br/>This is detected via a full parse of the package zip file. If an exception is raised when reading the zip file or one of it's entries, this error is reported. |
PKG-2 | All files contained in the Zip file SHALL be non compressed (STORED) or compressed using the "deflate" (DEFLATED) algorithm. Zip entry %s is compressed with an unknown algorithm.<br/>The zip library is used to examine the compression algorithm used, if it's outside of the prescribed values, this error is raised. |
PKG-3 | An OpenDocument package SHALL contain a file "META-INF/manifest.xml".<br/>Simply check whether such a zip entry exists. |
PKG-4 | An OpenDocument package SHOULD contain a file "mimetype".<br/>Simply check whether such a zip entry exists. |
PKG-5 | An OpenDocument package SHALL only contain the "META-INF/manifest.xml" and files containg the term "signatures" in their name in the "META-INF" folder. File %s does not meet this criteria.<br/>Each zip entry is examined. If a file is found below `META-INF/` that is not `manifest.xml` and whose name does not contain the term "signatures" this error is raised. |
PKG-7 | An OpenDocument package SHOULD contain a preview image Thumbnails/thumbnail.png.<br/>Simply check whether such a zip entry exists. |


### Manifest Errors

| Ref | Detection |
|-----|-------------|
| MAN-1 | The manifest SHALL contain an entry for every file in the package, zip entry %s has no corresponding manifest file entry.<br/>The software creates two lists of package contents:<ul><li>Zip Entries: Gathered from the zip archive directory</li><li>Manifest Entries: Gathered from `META-INF/manifest.xml` file.</li></ul>These are then compared and this error is fired only if there is a Zip Entry that doesn't have a corresponding Manfiest Entry. |
| MAN-2 | An OpenDocument package manifest SHALL NOT contain a file entry for itself.<br/>This simply detects the presence of a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value `META-INF/manifest.xml`. |
| MAN-3 | An OpenDocument package manifest SHALL NOT contain a file entry the mimetype file.<br/>This simply detects the presence of a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value `mimetype`. |
| MAN-4 | The manifest SHALL contain an entry for every file in the package, manifest file entry %s has no corresponding zip entry.<br/>The software creates two lists of package contents:<ul><li>Zip Entries: Gathered from the zip archive directory</li><li>Manifest Entries: Gathered from `META-INF/manifest.xml` file.</li></ul>These are then compared and this error is fired only if there is a Manifest Entry that doesn't have a corresponding Zip Entry. |
| MAN-5 | An OpenDocument package manifest SHALL contain a <manifest:file-entry> element whose manifest:full-path attribute has the value \"/\" if a mimetype file is present.<br/>This rule checks that a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value `/` exists. IF it doesn't AND a `mimetype` entry exists this error is fired, if a `mimetype` entry does not exist then the warning `MAN-7` is fired instead. |
| MAN-6 | The OpenDocument package manifest NEED NOT contain entries for file paths starting with META-INF/, %s.<br/>This rule checks that a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value starting with `META-INF/` exists. An information message that says this is unnecessary is raised. |
| MAN-7 | An OpenDocument package SHOULD contain a <manifest:file-entry> element whose manifest:full-path attribute has the value \"/\"".<br/>This rule checks that a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value `/` exists. IF it doesn't AND a `mimetype` entry also doesn't exist this warning is fired, if a `mimetype` entry does not exist then the error `MAN-5` is fired instead. |
| MAN-8 | For directories, the manifest file SHOULD contain a <manifest:file-entry> element only if a directory contains a document or a sub document. |
| MAN-9 | A directory for administrative or convenience purposes, such as a directory that contains various unrelated image files, SHOULD NOT have an entry in the manifest file. |

### MIME Media Type Errors

| Ref | Detection |
|-----|-------------|
| MIM-1 | The "mimetype" file SHALL be the first file of the zip file.<br/>The zip archives entries are extracted in physical order and the name of the first entry is recorded. IF the package contains a `mimetype` file AND it is not the first entry this error is raised. |
| MIM-2 | The "mimetype" file SHALL NOT be compressed.<br/>The software uses the zip library to examine the compression type used for the `mimetype` entry. If this is not `STORED` (which indicates no compression) then this error is raised. |
| MIM-3 | The "mimetype" file SHALL NOT use an 'extra field' in its header.<br/>The software uses the zip library to retrieve the list of extra fields for the `mimetype` entry. If the retured array has a size > 0 then this error is raised. |
| MIM-4 | An OpenDocument package SHALL contain a mimetype file IF the manifest contains a <manifest:file-entry> element whose manifest:full-path attribute has the value "/".<br/>IF the file doesn't contain a `mimetype` entry AND `META-INF/manifest.xml` DOES contain an entry with `manifest:full-path` "/" then this error is raised. If no such `<manifest:file-entry>` exists then the warning `PKG-4` is raised instead. |
| MIM-5 | An OpenDocument package mimetype file content SHALL be equal to the manifest:media-type attribute of the manifest <manifest:file-entry> element whose manifest:full-path attribute has the value \"/\".<br/>IF the file doesn't contain a `mimetype` entry AND `META-INF/manifest.xml` DOES contain an entry with `manifest:full-path` "/" then the value of the `mimetype` file contents and the `manifest:media-type` attribute are compared. If they aren't equal then this error is raised. |
| MIM-6 | The content of the "mimetype" file SHALL be ASCII encoded.<br/>Detection is impractical. |
