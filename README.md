# ODF Validator

## About

[Open Preservation Foundation](https://openpreservation.org/)'s OpenDocument Format Validator (OPF ODF Validator) enables your organisation to validate the file format standard and a set of file format policy rules created for improving the preservation effort of any files saved in the OpenDocument Format.

### File format support

The validator currently supports validation of spreadsheets with extensions .fods, .ods and .ots.

### Validation regulations references

* [OASIS OpenDocument Format](https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=office)
* [OPF Spreadsheets Preservation Specification](https://github.com/opf-labs/Spreadsheets-Preservation-Specification)

You may read more about the technical details of the validation checks [here](docs/VALIDATION.md).

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

For information about the validation process and messages see the [validation checks](docs/VALIDATION.md).
