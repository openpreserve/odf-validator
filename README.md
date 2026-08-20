# ODF Validator

Validates OpenDocument Format documents against the OASIS ODF specifications, v1.0 to v1.4. Optionally applies the [OPF Spreadsheets Preservation Specification](https://github.com/openpreserve/sheets-preservation-spec) as a policy profile.

Project website: <http://odf.openpreservation.org/>

## Install

Download from the [v0.20-beta-1 release](https://github.com/openpreserve/odf-validator/releases/tag/v0.20-beta-1).

| Platform | Asset | Runtime |
| --- | --- | --- |
| Linux | `odf-validator-0.20-beta-1-linux` | none |
| macOS (Apple Silicon) | `odf-validator-0.20-beta-1-mac` | none |
| Windows | `odf-validator-0.20-beta-1-windows.exe` | none |
| Any | `odf-validator-0.20-beta-1-all.jar` | Java 11+ |

```shell
chmod +x odf-validator-0.20-beta-1-linux
./odf-validator-0.20-beta-1-linux --version
```

```shell
java -jar odf-validator-0.20-beta-1-all.jar --version
```

Every release so far is flagged pre-release, so `/releases/latest` resolves to nothing. Use the tagged URL above.

## Usage

```shell
odf-validator [-dehVv] [-o=<outputFormat>] [[-f=<format>] | -p] FILE...
```

| Option | Effect |
| --- | --- |
| `-f`, `--format=<format>` | Validate as one format only. Takes an extension, MIME type or document element name, e.g. `spreadsheet`. |
| `-p`, `--profile` | Add the Spreadsheets Preservation Specification checks. Forces spreadsheet, so cannot be combined with `-f`. |
| `-e`, `--extended` | Extended document validation. |
| `-o`, `--output=<format>` | `TEXT` (default), `JSON` or `XML`. |
| `-d`, `--debug` | Debug output. |
| `-v`, `-vv`, `-vvv` | Increase verbosity. |

Exit code is `0` when the document passes, `1` when a check fails.

A document that passes:

```text
$ odf-validator dsigs-valid.ods
APP-1: [INFO] Validating dsigs-valid.ods.
APP-4: [INFO] Validation report for dsigs-valid.ods.
DOC-2: [INFO] package OpenDocument version 1.3 detected.
DOC-3: [INFO] mimetype OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
VALID, no errors, no warnings and 2 info message found.
```

One that does not:

```text
$ odf-validator bad_dsig_name.ods
APP-1: [INFO] Validating bad_dsig_name.ods.
APP-4: [INFO] Validation report for bad_dsig_name.ods.
DOC-2: [INFO] package OpenDocument version 1.3 detected.
DOC-3: [INFO] mimetype OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
PKG-5: [ERROR] META-INF/notsigs.xml An OpenDocument Package SHALL only contain the 'META-INF/manifest.xml' and files containg the term 'signatures' in their name in the 'META-INF' folder. File META-INF/notsigs.xml does not meet this criteria.
NOT VALID, 1 errors, 0 warnings and 2 info messages.
```

Every check id is documented: [validation checks](docs/validation/index.md), [policy checks](docs/policies/index.md), [detection detail](docs/detection/index.md). Further examples in [usage](docs/usage/index.md).

## Build

JDK 11 or newer, Maven 3.4 or newer.

```shell
git clone https://github.com/openpreserve/odf-validator.git
cd odf-validator
mvn clean package
```

Launcher scripts are written to the project root:

```shell
./odf-validator --help      # Linux, macOS
odf-validator.bat --help    # Windows
```

Note that `mvn validate` regenerates this README, the launcher scripts and the `docs/` pages from `src/main/resources/org/openpreservation/odf/{docs,batch}`. Edit the templates there, never the generated copies.

## Library

```xml
<dependency>
  <groupId>org.openpreservation.odf</groupId>
  <artifactId>odf-core</artifactId>
  <version>0.20-beta-1</version>
</dependency>
```

```java
OdfValidator validator = OdfValidators.getOdfValidator();
ValidationReport report = validator.validate(Path.of("package.ods"));
for (Check check : report.getChecks()) {
    System.out.printf("%s %s %s%n", check.getMessage().getId(),
                                    check.getMessage().getSeverity(),
                                    check.getMessage().getText());
}
```

[Developer docs](docs/developer/index.md) cover package parsing, the spreadsheet profile and JSON/XML report serialisation. [Javadoc](http://odf.openpreservation.org/site/apidocs/index.html).

## Licence

BSD 3-Clause, see [LICENSE](LICENSE).
