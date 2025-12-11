# ODF Spreadsheet Validator

Read more about:

* [Quick start for installing/building the software](#quick-start)
* [Using the command line software](./usage/).
* [A list of validation checks performed by the software](./validation/).
* [More details of the detection of validation conditions](./detection/).
* [How to use the validation library as a developer.](./developer/).
* [JavaDoc for the API and library.](./site/apidocs/index.html).
* [Source code on GitHub](https://github.com/openpreserve/odf-validator).
* [Project information](./site/project-info.html).
* [Project software reports](./site/project-reports.html).

## File format support

The validator validates Open Document Format documents of all formats. It supports all versions at time of writing, from v1.0 to v1.4.

## ODF validation references

* [OASIS OpenDocument Format](https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=office)
* [OPF Spreadsheets Preservation Specification](https://github.com/opf-labs/Spreadsheets-Preservation-Specification)

You may read more about the technical details of the validation checks [here](./validation/).

## Quick start

### Installation

Download the latest version of the validator. The latest builds can be found on the [GitHub releases page](https://github.com/openpreserve/odf-validator/releases/tag/v${project.version}). The software is available in four packages, a JAR file that requires a Java JRE v11 or greater, or platform specific executables for Windows, MacOS and Linux.

* [Java JAR, built using Java JDK 11](https://github.com/openpreserve/odf-validator/releases/download/v${project.version}/odf-validator-${project.version}-all.jar)
* [Windows executable, built using GraalVM](https://github.com/openpreserve/odf-validator/releases/download/v${project.version}/odf-validator-${project.version}-windows)
* [MacOS (Apple Silicon) executable, built using GraalVM](https://github.com/openpreserve/odf-validator/releases/download/v${project.version}/odf-validator-${project.version}-mac)
* [Linux executable, built using GraalVM](https://github.com/openpreserve/odf-validator/releases/download/v${project.version}/odf-validator-${project.version}-linux)

Simply download the appropriate package, make it executable and run it from the command line.

### Building the odf-validator

#### Prerequisites

To run the software you'll need a [Java 11](https://www.java.com/en/download/manual.jsp) JRE or newer.
To build the software you'll need a [Java 11](https://www.oracle.com/java/technologies/downloads/#java17) JDK or newer [Maven 3.4](https://maven.apache.org/download.cgi) or higher.

#### Getting the software

Clone [this GitHub repository](https://github.com/opf-labs/odf-validator.git) and move into the directory:

```shell
git clone https://github.com/opf-labs/odf-validator.git
cd odf-validator
```

#### Building the software

Now use Maven to build, test and package the software:

```shell
mvn clean package
```

#### Running the software

You can now use the accompanying shell script to run the software, for Windows user there is a batch file:

```shell
./odf-validator.bat --help
```

For Linux and MacOS users use the shell file:

```shell
./odf-validator --help
```
