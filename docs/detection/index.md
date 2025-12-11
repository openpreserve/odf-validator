# Detection of ODF Conditions

## ODF Specification Errors

### Document Errors

| Ref | Detection & Reference |
|-----|-------------|
| [[#DOC-1][]] | An OpenDocument document SHALL be a single XML file or a Zip file. |
| DOC-2 | OpenDocument version %s detected. An informational message that indicates the detected ODF version of the main document. |
| [[#DOC-3][]] | OpenDocument MIMETYPE %s detected. An informational message that indicates the detected MIME type. |
| [[#DOC-4][]] | Invalid MIMETYPE declaration %s detected. An error indicating that the MIME type detected is not a legal ODF media type. |
| [[#DOC-5][]] | No MIMETYPE declaration detected. While this is valid in some circumstances a warning is issued. |
| DOC-6 | OpenDocument document SHALL be format `<requested-format>`, no format was detected. This error is only issued if the `-f`/`--format` options is selected and the document doesn't contain a MIME type indicator. |
| #DOC-7 | OpenDocument document SHALL be format `<requested-format>`, but format `<detected-format>` was detected. This error is only issued if the `-f`/`--format` options is selected and the document's MIME type is not identical to the selected option. |
| [[#DOC-8][]] | This XML OpenDocument uses the following extended namespace prefixes `<prefix-list>`. This message provides a list of any extended/foreign namespaces detected in an ODF document. This is an error message if the `-e`/`--extended` option for extended conformance isn't selected. If extended validation is requested this message becomes informational. |

### Package Errors

| Ref | Detection & Reference |
|-----|-------------|
| [[#PKG-1][]] | An OpenDocument package SHALL be a well formed Zip Archive.<br/>This is detected via a full parse of the package zip file. If an exception is raised when reading the zip file or one of it's entries, this error is reported. |
| [[#PKG-2][]] | All files contained in the Zip file SHALL be non compressed (STORED) or compressed using the "deflate" (DEFLATED) algorithm. Zip entry %s is compressed with an unknown algorithm.<br/>The zip library is used to examine the compression algorithm used, if it's outside of the prescribed values, this error is raised. |
| [[#PKG-3][]] | An OpenDocument package SHALL contain a file "META-INF/manifest.xml".<br/>Simply check whether such a zip entry exists. |
| [[#PKG-4][]] | An OpenDocument package SHOULD contain a file "mimetype".<br/>Simply check whether such a zip entry exists. |
| [[#PKG-5][]] | An OpenDocument package SHALL only contain the "META-INF/manifest.xml" and files containing the term "signatures" in their name in the "META-INF" folder. File %s does not meet this criteria.<br/>Each zip entry is examined. If a file is found below `META-INF/` that is not `manifest.xml` and whose name does not contain the term "signatures" this error is raised. |
| [[#PKG-7][]] | An OpenDocument package SHOULD contain a preview image Thumbnails/thumbnail.png.<br/>Simply check whether such a zip entry exists. |
| [[#PKG-8][]] | Encrypted file entries shall be flagged as 'STORED' rather than 'DEFLATED' in the zip file's central directory. Zip entry %s is encrypted and flagged as 'DEFLATED'. |
| [[#PKG-10][]] |PKG-10 = Encrypted file entry detected. The software cannot decrypt encrypted ODF documents. This is flagged as a warning and the validation status is unknown as not all of the file contents could be accessed for validation. |

### Manifest Errors

| Ref | Detection |
|-----|-------------|
| [[#MAN-1][]] | The manifest SHALL contain an entry for every file in the package, zip entry %s has no corresponding manifest file entry.<br/>The software creates two lists of package contents:<ul><li>Zip Entries: Gathered from the zip archive directory</li><li>Manifest Entries: Gathered from `META-INF/manifest.xml` file.</li></ul>These are then compared and this error is fired only if there is a Zip Entry that doesn't have a corresponding Manifest Entry. |
| [[#MAN-2][]] | An OpenDocument package manifest SHALL NOT contain a file entry for itself.<br/>This simply detects the presence of a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value `META-INF/manifest.xml`. |
| [[#MAN-3][]] | An OpenDocument package manifest SHALL NOT contain a file entry the mimetype file.<br/>This simply detects the presence of a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value `mimetype`. |
| [[#MAN-4][]] | The manifest SHALL contain an entry for every file in the package, manifest file entry %s has no corresponding zip entry.<br/>The software creates two lists of package contents:<ul><li>Zip Entries: Gathered from the zip archive directory</li><li>Manifest Entries: Gathered from `META-INF/manifest.xml` file.</li></ul>These are then compared and this error is fired only if there is a Manifest Entry that doesn't have a corresponding Zip Entry. |
| [[#MAN-5][]] | An OpenDocument package manifest SHALL contain a <manifest:file-entry> element whose manifest:full-path attribute has the value \"/\" if a mimetype file is present.<br/>This rule checks that a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value `/` exists. IF it doesn't AND a `mimetype` entry exists this error is fired, if a `mimetype` entry does not exist then the warning `MAN-7` is fired instead. |
| [[#MAN-6][]] | The OpenDocument package manifest NEED NOT contain entries for file paths starting with META-INF/, %s.<br/>This rule checks that a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value starting with `META-INF/` exists. An information message that says this is unnecessary is raised. |
| [[#MAN-7][]] | An OpenDocument package SHOULD contain a <manifest:file-entry> element whose manifest:full-path attribute has the value \"/\"".<br/>This rule checks that a `META-INF/manifest.xml` `<manifest:file-entry>` element with a `manifest:full-path` attribute value `/` exists. IF it doesn't AND a `mimetype` entry also doesn't exist this warning is fired, if a `mimetype` entry does not exist then the error `MAN-5` is fired instead. |
| [[#MAN-8][]] | For directories, the manifest file SHOULD contain a <manifest:file-entry> element only if a directory contains a document or a sub document. |
| [[#MAN-9][]] | A directory for administrative or convenience purposes, such as a directory that contains various unrelated image files, SHOULD NOT have an entry in the manifest file. |

### MIME Media Type Errors

| Ref | Detection |
|-----|-------------|
| [[#MIM-1][]] | The "mimetype" file SHALL be the first file of the zip file.<br/>The zip archives entries are extracted in physical order and the name of the first entry is recorded. IF the package contains a `mimetype` file AND it is not the first entry this error is raised. |
| [[#MIM-2][]] | The "mimetype" file SHALL NOT be compressed.<br/>The software uses the zip library to examine the compression type used for the `mimetype` entry. If this is not `STORED` (which indicates no compression) then this error is raised. |
| [[#MIM-3][]] | The "mimetype" file SHALL NOT use an 'extra field' in its header.<br/>The software uses the zip library to retrieve the list of extra fields for the `mimetype` entry. If the retured array has a size > 0 then this error is raised. |
| [[#MIM-4][]] | An OpenDocument package SHALL contain a mimetype file IF the manifest contains a <manifest:file-entry> element whose manifest:full-path attribute has the value "/".<br/>IF the file doesn't contain a `mimetype` entry AND `META-INF/manifest.xml` DOES contain an entry with `manifest:full-path` "/" then this error is raised. If no such `<manifest:file-entry>` exists then the warning `PKG-4` is raised instead. |
| [[#MIM-5][]] | An OpenDocument package mimetype file content SHALL be equal to the manifest:media-type attribute of the manifest <manifest:file-entry> element whose manifest:full-path attribute has the value \"/\".<br/>IF the file doesn't contain a `mimetype` entry AND `META-INF/manifest.xml` DOES contain an entry with `manifest:full-path` "/" then the value of the `mimetype` file contents and the `manifest:media-type` attribute are compared. If they aren't equal then this error is raised. |
| [[#MIM-6][]] | The content of the "mimetype" file SHALL be ASCII encoded.<br/>Detection is impractical. |

[#DOC-1]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#a_2_2_Document_Conformance
[#DOC-3]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream
[#DOC-4]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream
[#DOC-5]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream
[#DOC-8]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_2_2_2_OpenDocument_Extended_Document
[#PKG-1]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#OpenDocument_Package
[#PKG-2]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#OpenDocument_Package
[#PKG-3]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest
[#PKG-4]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream
[#PKG-5]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#OpenDocument_Package
[#PKG-7]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_8_Preview_Image
[#PKG-8]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#Encryption-General
[#PKG-10]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#Encryption
[#MAN-1]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest
[#MAN-2]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest
[#MAN-3]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest
[#MAN-4]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest
[#MAN-5]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest
[#MAN-6]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest
[#MAN-7]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest
[#MAN-8]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#element-manifest-file-entry
[#MAN-9]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#element-manifest-file-entry
[#MIM-1]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream
[#MIM-2]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream
[#MIM-3]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream
[#MIM-4]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream
[#MIM-5]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream
[#MIM-6]: https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#MIME_type_stream

