# ODF Spreadsheet Validator

Read more about:

* [Using the command line software](./usage/).
* [A list of validation checks performed by the software](./validation/).
* [A list of policy checks performed by the software](./policies/).
* [More details of the detection of validation conditions](./detection/).
* [How to use the validation library as a developer.](./developer/).
* [JavaDoc for the API and library.](./site/apidocs/index.html).
* [Source code on GitHub](https://github.com/openpreserve/odf-validator).
* [Project information](./site/project-info.html).
* [Project software reports](./site/project-reports.html).

## File format support

The validator currently supports validation of spreadsheets with extensions .fods, .ods and .ots.

## Validation regulations references

* [OASIS OpenDocument Format](https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=office)
* [OPF Spreadsheets Preservation Specification](https://github.com/opf-labs/Spreadsheets-Preservation-Specification)

You may read more about the technical details of the validation checks [here](./validation/).

## Quick Start

For developer instructions with Maven locations and examples please see [DEVELOPER.md](./developer/).

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
