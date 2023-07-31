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

### Top Level Checks

1. Check if the file is a ZIP archive, if yes validate as a package.
2. If not a ZIP archive then try to validate as a single XML file.

### Package Validation

1. It shall be a Zip file, as defined by [ZIP]. All files contained in the Zip file shall be non compressed (STORED) or compressed using the “deflate” (DEFLATED) algorithm.
2. It shall contain a file “META-INF/manifest.xml”. This file shall meet the following requirements:
  a. The file shall be a well formed XML document in accordance with the [XML1.0] specification.
  b. The XML root element of the file shall be a `<manifest:manifest>` element 4.2.
  c. The XML file shall be valid with respect to the manifest schema defined in appendix A.1 OpenDocument Manifest Schema.
3. It should contain a file “mimetype”.
4. It may contain files whose relative paths begin with “META-INF/” and whose names contain the string “signatures”. These file shall meet the following requirements:
  a. The files shall be well-formed XML files in accordance with [XML1.0].
  b. The XML root element of each file shall be a `<dsig:document-signatures>` element 5.2.
  c. The files shall be valid with respect to the digital signature schema defined in appendix A.2 OpenDocument Digital Signature Schema.
5. It shall not contain other files whose relative path begins with “META-INF/” other than those listed in (2) and (4), nor shall the META-INF/manifest.xml contain any implementation-defined IRIs as alternative identifications.
6. The files listed in (2) and (4) meet the following requirements:
  a. They shall be namespace-well-formed with regard to the XML Namespaces specification [xml-names].
  b. They shall conform to the xml-id specification [XML-ID].

#### Manifest Validation

All OpenDocument packages shall contain a file named “META-INF/manifest.xml”. This file is the OpenDocument package manifest. The manifest provides :

- A list of all of the files in the package (except those specifically excluded from the manifest).
- The MIME media type of each file in the package.
- If a file is stored in the file data in encrypted form, the manifest provides information required to decrypt the file correctly when the encryption key is also supplied.

The format of the manifest file is specified in chapter 4.

For all files contained in a package, with exception of the “mimetype” file and files whose relative path starts with “META-INF/”, the “META-INF/manifest.xml” file shall contain exactly one `<manifest:file-entry>` element whose `manifest:full-path` attribute's value references the file.

The “META-INF/manifest.xml” file need not contain `<manifest:file-entry>` elements 4.3 whose manifest:full-path attribute 4.16.4 references files whose relative path start with "META-INF/". The file shall not contain `<manifest:file-entry>` elements whose manifest:full-path attribute value references the “META-INF/manifest.xml” file itself or the “mimetype” file.

The “META-INF/manifest.xml” file should contain a `<manifest:file-entry>` element whose manifest:full-path attribute has the value "/". This element specifies information regarding the document stored in the root of the package. This entry shall exist if the package contains a file "mimetype"

#### MIME Media Type Validation

If a MIME media type for a document exists, then an OpenDocument package should contain a file with name “mimetype”. The content of this file shall be the ASCII encoded MIME media type associated with the document. See [RFC6838].

The “mimetype” file shall be the first file of the zip file. It shall not be compressed, and it shall not use an 'extra field' in its header.

If the file named “META-INF/manifest.xml” contains a `<manifest:file-entry>` element whose manifest:full-path attribute has the value "/", then a "mimetype" file shall exist, and the content of the “mimetype” file shall be equal to the value of the manifest:media-type attribute 4.16.10 of that element.

Note: The purpose is to allow the type of document represented by the package to be discovered through 'magic number' mechanisms, such as Unix's file/magic utility. If a Zip file contains a file at the beginning of the file that is uncompressed, and has no extra data in the header, then its file name and data can be found at fixed positions from the beginning of the package. More specifically, one will find:

- the string 'PK' at position 0 of all zip files
- the string 'mimetype' beginning at position 30
- the media type itself beginning at position 38.

### Single XML Validation

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

