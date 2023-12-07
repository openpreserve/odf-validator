# ODF Validation Checks

## File Checks

### DOC-1 (Error)

An OpenDocument SHALL be an OpenDocument Package or a single XML file.

See [Section 2.2.1 of the ODF Schema Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#a_2_2_1_OpenDocument_Document).

### DOC-2 (Info)

OpenDocument version `<version>` detected.

Reports the OpenDocument version of the document.

### DOC-3 (Info)

OpenDocument MIMETYPE `<mimetype>` detected.

Reports the OpenDocument MIMETYPE of the document.

### DOC-4 (Error)

Invalid MIMETYPE declaration %s detected.

The `<office:document>` element SHALL have an `office:mimetype` attribute with a value from the list of MIME types for use with OpenDocuments that have been registered with IANA.

See item E in [Section 2.2.1 of the ODF Schema Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#a_2_2_1_OpenDocument_Document).

## Package Checks

### PKG-1 (Error)

An OpenDocument Package SHALL be a well formed Zip Archive.

It shall be a Zip file as defined in PKWARE Inc. Zip APPNOTE Version 6.2.0, <http://www.pkware.com/support/application-note-archives>, 2004.

See [Section 2.2.1 of the ODF Schema Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#a_2_2_1_OpenDocument_Document).

### PKG-2 (Error)

All files contained in the Zip file shall be non compressed (STORED) or compressed using the “deflate” (DEFLATED) algorithm. Zip entry `<entry-name>` is compressed with an unknown algorithm.

All files contained in the Zip file shall be non compressed (STORED) or compressed using the “deflate” (DEFLATED) algorithm. See item A in [Section 2.2.1 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_2_2_1_OpenDocument_Package).

### PKG-3 (Error)

An OpenDocument Package SHALL contain a file `META-INF/manifest.xml`.

See item B in [Section 2.2.1 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_2_2_1_OpenDocument_Package).

### PKG-4 (Warning)

An OpenDocument Package SHOULD contain a file `mimetype`.

See item C in [Section 2.2.1 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_2_2_1_OpenDocument_Package). See also See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

This may become a SHALL depending on the contents of the `manifest.xml` file, see MIM-4 below.

### PKG-5 (Error)

An OpenDocument Package SHALL only contain the `META-INF/manifest.xml` and files containg the term "signatures" in their name in the `META-INF/` folder. File entry `<entryName>` does not meet this criteria.

It (an OpenDocument Package) may contain files whose relative paths begin with “META-INF/” and whose names contain the string “signatures”. These file shall meet the following requirements:

* D.1: The files shall be well-formed XML files in accordance with [XML1.0].
* D.2: The XML root element of each file shall be a <dsig:document-signatures> element 5.2.
* D.3: The files shall be valid with respect to the digital signature schema defined in appendix A.2 OpenDocument Digital Signature Schema.

### PKG-7 (Warning)

An OpenDocument Package SHOULD contain a preview image Thumbnails/thumbnail.png.

Unless a document is encrypted, package producers should generate a preview image of the document that is contained in the package. The preview image shall be contained in a file named “Thumbnails/thumbnail.png”.

See [Section 3.8 of the ODF Schema Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_8_Preview_Image).

### PKG-8 (Error)

Encrypted file entries shall be flagged as "STORED" rather than "DEFLATED" in the zip file's central directory. Zip entry `<entryName>` is encrypted and flagged as "DEFLATED".

## Manifest Validation

An OpenDocument Package SHALL contain a file “META-INF/manifest.xml”. This file shall meet the following requirements:

a. The file shall be a well formed XML document in accordance with the [XML1.0] specification.
b. The XML root element of the file shall be a `<manifest:manifest>` element 4.2.
c. The XML file shall be valid with respect to the manifest schema defined in appendix A.1 OpenDocument Manifest Schema.

Note that the Manifest file is subject to XML validation checks, see [XML Validation](#xml-validation) below.

### MAN-1 (Error)

The manifest SHALL contain an entry for every file in the package, zip entry `<entry-name>` has no corresponding manifest file entry.

For all files contained in a package, with exception of the “mimetype” file and files whose relative path starts with “META-INF/”, the “META-INF/manifest.xml” file shall contain exactly one `<manifest:file-entry>` element whose `manifest:full-path` attribute's value references the file.

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

### MAN-2 (Error)

An OpenDocument Package manifest SHALL NOT contain a file entry for itself.

The file shall not contain <manifest:file-entry> elements whose manifest:full-path attribute value references the “META-INF/manifest.xml” file itself or the “mimetype” file.

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

### MAN-3 (Error)

An OpenDocument Package manifest SHALL NOT contain a file entry the mimetype file.

The file shall not contain <manifest:file-entry> elements whose manifest:full-path attribute value references the “META-INF/manifest.xml” file itself or the “mimetype” file.

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

### MAN-4 (Error)

The manifest SHALL contain an entry for every file in the package, manifest file entry %s has no corresponding zip entry.

For all files contained in a package, with exception of the “mimetype” file and files whose relative path starts with “META-INF/”, the “META-INF/manifest.xml” file shall contain exactly one `<manifest:file-entry>` element whose `manifest:full-path` attribute's value references the file.

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

### MAN-5 (Error)

An OpenDocument Package manifest SHALL contain a root \"/\" entry if a mimetype file is present.

The “META-INF/manifest.xml” file should contain a <manifest:file-entry> element whose manifest:full-path attribute has the value "/". This element specifies information regarding the document stored in the root of the package. This entry shall exist if the package contains a file "mimetype"

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

### MAN-6 (Info)

The OpenDocument Package manifest NEED NOT contain entries for file paths starting with `META-INF/`, `<entryName>`.

The “META-INF/manifest.xml” file need not contain `<manifest:file-entry>` elements 4.3 whose manifest:full-path attribute 4.16.4 references files whose relative path start with "META-INF/".

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

### MAN-7 (Warning)

An OpenDocument Package SHOULD contain a `<manifest:file-entry>` element whose manifest:full-path attribute has the value \"/\"".

The “META-INF/manifest.xml” file should contain a `<manifest:file-entry>` element whose `manifest:full-path` attribute has the value "/". This element specifies information regarding the document stored in the root of the package. This entry shall exist if the package contains a file "mimetype"

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

### MAN-8 (Warning)

For directories, the manifest file should contain a `<manifest:file-entry>` element only if a directory contains a document or a sub document.

See [Section 4.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_4_3__manifest_file-entry_).

### MAN-9 (Warning)

A directory for administrative or convenience purposes, such as a directory that contains various unrelated image files, should not have an entry in the manifest file.

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

## MIME Media Type Validation

If a MIME media type for a document exists, then an OpenDocument Package SHOULD contain a file with name “mimetype”.

### MIM-1 (Error)

The `mimetype` file SHALL be the first file of the zip file.

The `mimetype` file shall be the first file of the zip file. It shall not be compressed, and it shall not use an 'extra field' in its header.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

### MIM-2 (Error)

The "mimetype" file SHALL NOT be compressed.

The “mimetype” file shall be the first file of the zip file. It shall not be compressed, and it shall not use an 'extra field' in its header.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

### MIM-3 (Error)

The “mimetype” file SHALL NOT use an 'extra field' in its header.

The “mimetype” file shall be the first file of the zip file. It shall not be compressed, and it shall not use an 'extra field' in its header.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type). See <https://users.cs.jmu.edu/buchhofp/forensics/formats/pkzip.html#localheader>.

### MIM-4 (Error)

An OpenDocument Package manifest SHALL contain a mimetype file IF a root "/" entry is present.

If the file named “META-INF/manifest.xml” contains a `<manifest:file-entry>` element whose `manifest:full-path` attribute has the value "/", then a "mimetype" file shall exist

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

### MIM-5 (Error)

An OpenDocument Package manifest root \"/\" entry's mimetype value SHALL be equal to the value read from the mimetype file if present.

If the file named “META-INF/manifest.xml” contains a `<manifest:file-entry>` element whose manifest:full-path attribute has the value "/", then a "mimetype" file shall exist, and the content of the “mimetype” file shall be equal to the value of the `manifest:media-type` attribute of that element.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

### MIM-6

The content of the "mimetype" file SHALL be ASCII encoded.

If a MIME media type for a document exists, then an OpenDocument Package should contain a file with name “mimetype”. The content of this file shall be the ASCII encoded MIME media type associated with the document.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

This criteria is to ensure easy "magic" identification of ODF packages, see the further note in 3.3:

> **Note:** The purpose is to allow the type of document represented by the package to be discovered through 'magic number' mechanisms, such as Unix's file/magic utility. If a Zip file contains a file at the beginning of the file that is uncompressed, and has no extra data in the header, then its file name and data can be found at fixed positions from the beginning of the package. More specifically, one will find:
>
> * the string 'PK' at position 0 of all zip files
> * the string 'mimetype' beginning at position 30
> * the media type itself beginning at position 38.

This mechanism can only work IF the mimetype file is ASCII/UTF-8/UTF-7 encoded with no Byte Order Mark (BOM). If the mimetype file is encoded in UTF-16 or UTF-32, or has a BOM, then the magic number identification will fail.

Whether this constitutes grounds for a specific validation check, or whether such a check is implementable is the question.

## XML Validation

1. File shall be a well formed XML document.
2. File shall conform to the XML Namespaces specification [XML-Names].
3. File shall conform to the xml-id Version 1.0 specification [XML-ID].
4. The XML root element shall be `<office:document>`.
5. The document shall validate agains the the ODF 1.3 schema.
6. If a `style:condition`, `table:condition`, `table:expression`, `table:formula` or `text:formula` attribute value begins with a namespace prefix bound to namespace  `urn:oasis:names:tc:opendocument:xmlns:of:1.3`, the syntax and semantics of the attribute value portions that are expressions determined by the prefix shall conform to Part4 of this specification. If a `style:condition`, `table:condition`, `table:expression`, `table:formula` or `text:formula` attribute value has no namespace prefix, the attribute value portions that are expressions determined by a prefix shall conform as if there were a prefix bound to namespace `urn:oasis:names:tc:opendocument:xmlns:of:1.3`.
7. A spreadsheet's root `<office:document>` element shall have an `office:mimetype` attribute with value:
   * `application/vnd.oasis.opendocument.spreadsheet`; or
   * `application/vnd.oasis.opendocument.spreadsheet-template`;
8. The `<office:body>` element shall have the `<office:spreadsheet>` element as its child;
9. All namespace prefixes used in the values of `table:formula` attributes values shall be bound to the `urn:oasis:names:tc:opendocument:xmlns:of:1.2` namespace;
10. All `table:formula` attribute values shall be a conforming OpenDocument Formula expression [ODF1.3-Part-4:OpenFormula] 2.2 OpenDocument Formula Expression.

### XML-1

SaxException: `SAXException message`

A low level XML parsing exception has occurred for a file that was expected to be well formed XML.

### XML-2

Root element of XML file %s, must be %s but found value %s.

### XML-3

Not a well formed XML document. XML parsing exception at line `<lineNumber>` and column `<columnNumber>`: `XMLException message`.

The XML document is not well formed according to the XML standard. The parsing exception is also reported with location information.

### XML-4

Not a valid XML document. Validation exception at line `<lineNumber>` and column `<columnNumber>`: `XMLException message`.

The XML document is not valid according to an XML schema document. The validation exceptions is also reported with the location information.
