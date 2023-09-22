# ODF Validator

## About

[Open Preservation Foundation](https://openpreservation.org/)'s OpenDocument Format Validator (OPF ODF Validator) enables your organisation to validate the file format standard and a set of file format policy rules created for improving the preservation effort of any files saved in the OpenDocument Format.

### File format support

The validator currently supports validation of spreadsheets with extensions .fods, .ods and .ots.

### Validation regulations references

* [OASIS OpenDocument Format](https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=office)
* [OPF Spreadsheets Preservation Specification](https://github.com/opf-labs/Spreadsheets-Preservation-Specification)

You may read more about the technical details of the validation checks [here](#validation-checks).


## Quick Start

### Prerequisites

To run the software you'll need a [Java 8](https://www.java.com/en/download/manual.jsp) JRE or newer.
To build the software you'll need a [Java 8](https://www.oracle.com/java/technologies/downloads/#java17) JDK or newer [Maven 3.4](https://maven.apache.org/download.cgi) or higher.

### Getting the software

Clone [this GitHub repository](https://github.com/opf-labs/odf-validator.git) and move into the directory:

```shell
git clone https://github.com/opf-labs/odf-validator.git
cd odf-validator
```

### Building the software

Now use Maven to build, test and package the software:

```shell
mvn clean package
```

### Running the software

You can now use the accompanying shell script to run the software, for Windows user there is a batch file:

```shell
./odf-validator.bat --help
```

For Linux and MacOS users use the shell file:

```shell
./odf-validator --help
```

## Validation Checks

Validation is performed in a sequence of logical steps:

1. File Checks: Checks that the submitted file is a suitable format for parsing/validation.
2. Package Validation: Checks that the submitted file is a valid ODF package with respect to the ODF Package Specification, if the file submitted is a zip archive.
3. Manifest Validation: Checks that the submitted packages's "META-INF/manifest.xml" file is valid with respect to the ODF Package Specification.
4. MIME Media Type Validation: Checks that the submitted packages's "mimetype" file is valid with respect to the ODF Package Specification.
5. XML Validation: Checks that the submitted file is a valid ODF XML file with respect to the ODF Schema Specification and appropriate XML schema. This is the only check performed on a single XML file, for a package multiple XML files may be checked.

### File Checks

#### DOC-1

An OpenDocument SHALL be an OpenDocument package or a single XML file.

See [Section 2.2.1 of the ODF Schema Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#a_2_2_1_OpenDocument_Document).

This is an initial check to ensure that the submitted file is a suitable format for parsing/validation. The check is performed by a simple "magic number" format identification match. The check will fail if the file is not either a:

* single XML file, processed as an ODF "flat" file; or
* ZIP archive, proceessed as a package;
  
Note the applied check is not a full parse of the file, simply a test of the first few bytes. There is no guarantee that the a file that passes this check will be parsable as a ZIP archive or XML file.

### Package Validation

#### PKG-1

Zip entry `<entry-name>` is compressed with unknown algorithm.

All files contained in the Zip file shall be non compressed (STORED) or compressed using the “deflate” (DEFLATED) algorithm. See item A in [Section 2.2.1 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_2_2_1_OpenDocument_Package).

#### PKG-2

An OpenDocument package SHOULD contain a file “mimetype”.

See item C in [Section 2.2.1 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_2_2_1_OpenDocument_Package). See also See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

This may become a SHALL depending on the contents of the `manifest.xml` file, see PKG-10 below.

#### PKG-3

Subdirectory `<directory-name>` not allowed in the "META-INF" folder.

TODO: This requires clarification, related to extended conformance.

#### PKG-4

No manifest.xml found in package "META-INF" folder.

An OpenDocument package SHALL contain a file “META-INF/manifest.xml”.

See item B in [Section 2.2.1 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_2_2_1_OpenDocument_Package).

#### PKG-9

An OpenDocument package SHALL be a well formed Zip Archive.

An OpenDocument SHALL be an OpenDocument package or a single XML file.

See [Section 2.2.1 of the ODF Schema Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part3-schema/OpenDocument-v1.3-os-part3-schema.html#a_2_2_1_OpenDocument_Document).

It shall be a Zip file, as defined by [ZIP]. This is detected via a full parse of the package zip file. If an error occurs when reading the zip file or one of it's entries, this error is reported.

#### PKG-18

An OpenDocument package SHOULD contain a preview image Thumbnails/thumbnail.png.

Unless a document is encrypted, package producers should generate a preview image of the document that is contained in the package. The preview image shall be contained in a file named “Thumbnails/thumbnail.png”.

See [Section 3.8 of the ODF Schema Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_8_Preview_Image).

### Manifest Validation

An OpenDocument package SHALL contain a file “META-INF/manifest.xml”. This file shall meet the following requirements:

a. The file shall be a well formed XML document in accordance with the [XML1.0] specification.
b. The XML root element of the file shall be a `<manifest:manifest>` element 4.2.
c. The XML file shall be valid with respect to the manifest schema defined in appendix A.1 OpenDocument Manifest Schema.

Note that the Manifest file is subject to XML validation checks, see [XML Validation](#xml-validation) below.

#### PKG-11

An OpenDocument package manifest SHALL contain a root \"/\" entry if a mimetype file is present.

The “META-INF/manifest.xml” file should contain a <manifest:file-entry> element whose manifest:full-path attribute has the value "/". This element specifies information regarding the document stored in the root of the package. This entry shall exist if the package contains a file "mimetype"

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

#### PKG-12

PKG-12 = An OpenDocument package manifest root \"/\" entry's mimetype value SHALL be equal to the value in the mimetype file if present.

If the file named “META-INF/manifest.xml” contains a `<manifest:file-entry>` element whose manifest:full-path attribute has the value "/", then a "mimetype" file shall exist, and the content of the “mimetype” file shall be equal to the value of the `manifest:media-type` attribute of that element.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

#### PKG-13

The OpenDocument package manifest NEED NOT contain entries for file paths starting with META-INF/, %s.

The “META-INF/manifest.xml” file need not contain <manifest:file-entry> elements 4.3 whose manifest:full-path attribute 4.16.4 references files whose relative path start with "META-INF/".

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

#### PKG-14

An OpenDocument package manifest SHALL NOT contain a file entry for itself.

The file shall not contain <manifest:file-entry> elements whose manifest:full-path attribute value references the “META-INF/manifest.xml” file itself or the “mimetype” file.

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

#### PKG-15

An OpenDocument package manifest SHALL NOT contain a file entry the mimetype file.

The file shall not contain <manifest:file-entry> elements whose manifest:full-path attribute value references the “META-INF/manifest.xml” file itself or the “mimetype” file.

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

#### PKG-16

The manifest SHALL contain an entry for every file in the package, manifest file entry %s has no corresponding zip entry.

For all files contained in a package, with exception of the “mimetype” file and files whose relative path starts with “META-INF/”, the “META-INF/manifest.xml” file shall contain exactly one `<manifest:file-entry>` element whose `manifest:full-path` attribute's value references the file.

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

#### PKG-17

The manifest SHALL contain an entry for every file in the package, zip entry `<entry-name>` has no corresponding manifest file entry.

For all files contained in a package, with exception of the “mimetype” file and files whose relative path starts with “META-INF/”, the “META-INF/manifest.xml” file shall contain exactly one `<manifest:file-entry>` element whose `manifest:full-path` attribute's value references the file.

See [Section 3.2 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_2_Manifest).

#### MIME Media Type Validation

If a MIME media type for a document exists, then an OpenDocument package should contain a file with name “mimetype”.
#### PKG-5

The content of the "mimetype" file SHALL be ASCII encoded.

If a MIME media type for a document exists, then an OpenDocument package should contain a file with name “mimetype”. The content of this file shall be the ASCII encoded MIME media type associated with the document.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

#### PKG-6

The "mimetype" file SHALL NOT be compressed.

The “mimetype” file shall be the first file of the zip file. It shall not be compressed, and it shall not use an 'extra field' in its header.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

#### PKG-7

The “mimetype” file SHALL be the first file of the zip file.

The “mimetype” file shall be the first file of the zip file. It shall not be compressed, and it shall not use an 'extra field' in its header.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

#### PKG-8

The “mimetype” file SHALL NOT use an 'extra field' in its header.

The “mimetype” file shall be the first file of the zip file. It shall not be compressed, and it shall not use an 'extra field' in its header.

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

#### PKG-10

An OpenDocument package manifest SHALL contain a mimetype file IF a root "/" entry is present.

If the file named “META-INF/manifest.xml” contains a `<manifest:file-entry>` element whose `manifest:full-path` attribute has the value "/", then a "mimetype" file shall exist

See [Section 3.3 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_3_MIME_Media_Type).

### XML Validation

XML-1 = SaxException: %s
XML-2 = Root element of XML file %s, must be %s but found value %s.
XML-3 = Not a well formed XML document. XML parsing exception at line %d and column %d: %s.
XML-4 = Not a valid XML document. Validation exception at line %d and column %d: %s.

1. File shall be a well formed XML document.
2. File shall conform to the XML Namespaces specification [XML-Names].
3. File shall conform to the xml-id Version 1.0 specification [XML-ID].
4. The XML root element shall be `<office:document>`.
5. The document shall validate agains the the ODF 1.3 schema.
6. If a `style:condition`, `table:condition`, `table:expression`, `table:formula` or `text:formula` attribute value begins with a namespace prefix bound to namespace  `urn:oasis:names:tc:opendocument:xmlns:of:1.3`, the syntax and semantics of the attribute value portions that are expressions determined by the prefix shall conform to Part4 of this specification. If a `style:condition`, `table:condition`, `table:expression`, `table:formula` or `text:formula` attribute value has no namespace prefix, the attribute value portions that are expressions determined by a prefix shall conform as if there were a prefix bound to namespace `urn:oasis:names:tc:opendocument:xmlns:of:1.3`.
7. A spreadsheet's root `<office:document>` element shall have an `office:mimetype` attribute with value:
   - `application/vnd.oasis.opendocument.spreadsheet`; or
   - `application/vnd.oasis.opendocument.spreadsheet-template`;
8. The `<office:body>` element shall have the `<office:spreadsheet>` element as its child;
9. All namespace prefixes used in the values of `table:formula` attributes values shall be bound to the `urn:oasis:names:tc:opendocument:xmlns:of:1.2` namespace;
10. All `table:formula` attribute values shall be a conforming OpenDocument Formula expression [ODF1.3-Part-4:OpenFormula] 2.2 OpenDocument Formula Expression.

