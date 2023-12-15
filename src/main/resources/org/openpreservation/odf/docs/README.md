# ODF Validator

Latest version is ${project.version}.

## About

[Open Preservation Foundation](https://openpreservation.org/)'s OpenDocument Format Validator (OPF ODF Validator) enables your organisation to validate the file format standard and a set of file format policy rules created for improving the preservation effort of any files saved in the OpenDocument Format.

### File format support

The validator currently supports validation of spreadsheets with extensions .fods, .ods and .ots.

### Validation regulations references

* [OASIS OpenDocument Format](https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=office)
* [OPF Spreadsheets Preservation Specification](https://github.com/opf-labs/Spreadsheets-Preservation-Specification)

You may read more about the technical details of the validation checks [here](docs/VALIDATION.md).

## Quick Start

For developer instructions with Maven locations and examples please see [DEVELOPER.md](docs/DEVELOPER.md).

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

## Usage

### Command line options

The command line options are currently as follows:

```shell
$ odf-validator --help
Usage: odf-validator [-hpV] FILE...
Validates Open Document Format spreadsheets.
      FILE...     A list of Open Document Format spreadsheet files to validate.
  -h, --help      Show this help message and exit.
  -p, --profile   Validate using extended Spreadsheet preservation profile.
  -V, --version   Print version information and exit.
```

### Examples

#### Validating a single file using the OpenDocument Format specification

The first example shows the output generated when using the vanilla validator on a single valid ODF spreadsheet package:

```bash
$ odf-validator ./Valid/Simple.ods
APP-1: [INFO] Validating ./Valid/Simple.ods.
APP-4: [INFO] Validation report for ./Valid/Simple.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
VALID, no errors, no warnings and 1 info message found.
```

The next example shows the output generated when using the vanilla validator on a single invalid ODF spreadsheet package:

```bash
$ odf-validator ./MIM_1/MimetypeNotFirstFile.ods
APP-1: [INFO] Validating ./MIM_1/MimetypeNotFirstFile.ods.
APP-4: [INFO] Validation report for ./MIM_1/MimetypeNotFirstFile.ods.
MIM-1: mimetype [ERROR] The "mimetype" file SHALL be the first file of the zip file.
MIM-2: mimetype [ERROR] The "mimetype" file SHALL NOT be compressed.
MIM-3: mimetype [ERROR] The "mimetype" file SHALL NOT use an 'extra field' in its header.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
NOT VALID, 3 errors, 0 warnings and 1 info messages.```
```

This example shows the output generated when using the vanilla validator on a single ODF spreadsheet package that is valid but generates warnings:

```bash
$ odf-validator ./PKG_7/PreviewImageMissing.ods
APP-1: [INFO] Validating ./PKG_7/PreviewImageMissing.ods.
APP-4: [INFO] Validation report for ./PKG_7/PreviewImageMissing.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
PKG-7: Thumbnails/thumbnail.png [WARNING] An OpenDocument Package SHOULD contain a preview image Thumbnails/thumbnail.png.
VALID, no errors, 1 warnings found and 1 info messages.
```

#### Validating a single file using the Preservation Specification profile

The example below shows the output generated when using the preservation profile validator on a single valid ODF spreadsheet package, note the use of the `--profile` flag:

```bash
$ odf-validator --profile ./Valid/Simple.ods
APP-1: [INFO] Validating ./Valid/Simple.ods.
APP-5: [INFO] DNA ODF Spreadsheets Preservation Specification Profile report for ./Valid/Simple.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
VALID, no errors, no warnings and 1 info message found.
```

Here's a file that fails one of the profile checks:

```bash
$ odf-validator --profile ./ODF_7/WithZeroCellData.ods
APP-1: [INFO] Validating ./ODF_7/WithZeroCellData.ods. 
APP-5: [INFO] DNA ODF Spreadsheets Preservation Specification Profile report for ./ODF_7/WithZeroCellData.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
POL_7: content.xml [ERROR] Content check | The file MUST have values or objects in at least one cell.
NOT VALID, 1 errors, 0 warnings and 1 info messages.
```

Finally and example of a Preservation Profile rule that simply reports the presesnce of embedded objects:

```bash
$ odf-validator --profile ./ODF_6/WithTwoEmbeddedWordDocument.ods
APP-1: [INFO] Validating ./ODF_6/WithTwoEmbeddedWordDocument.ods.
APP-5: [INFO] DNA ODF Spreadsheets Preservation Specification Profile report for ./ODF_6/WithTwoEmbeddedWordDocument.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
POL_6: content.xml [INFO] Embedded Objects | The spreadsheet MAY reference Embedded OpenDocument objects.
POL_6: content.xml [INFO] Embedded Objects | The spreadsheet MAY reference OLE objects.
VALID, no errors, no warnings and 3 info message found.
```

## Validation Checks

For information about the validation process and messages see the [validation checks](docs/VALIDATION.md).
