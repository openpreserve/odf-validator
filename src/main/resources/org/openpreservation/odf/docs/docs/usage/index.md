# Usage

## Command line options

The command line options are currently as follows:

```text
$ odf-validator --help
Usage:
Validates Open Document Format documents.

odf-validator [-dehVv] [-o=<outputFormat>] [[-f=<format>] | -p] FILE...

Description:

Validates Open Document Format documents against the appropriate ODF
specifications. May optionally include additional preservation checks.

Parameters:
      FILE...             A list of Open Document Format document files to
                            validate.
Options:
  -e, --extended          Process XML documents to allow for extended document
                            validation.
  -o, --output=<outputFormat>
                          Output results as TEXT, JSON or XML.
  -d, --debug             Enable debug output.
  -v                      Specify multiple -v options to increase verbosity.
                          For example, `-v -v -v` or `-vvv`
  -h, --help              Show this help message and exit.
  -V, --version           Print version information and exit.

Format Options (mutually exclusive):
  -f, --format=<format>   Validate a particular ODF format only
                          This value can be an extension, a MIME type, or a
                            document element name, e.g. spreadsheet.
  -p, --profile           Validate using additional Spreadsheet preservation
                            profile.
                          Cannot be used with the format option as Spreadsheet
                            is forced by the profile.
```

## Examples

### Validating a single file using the OpenDocument Format specification

The first example shows the output generated when using the vanilla validator on a single valid ODF spreadsheet package:

```text
$ odf-validator ./Valid/Simple.ods
APP-1: [INFO] Validating ./Valid/Simple.ods.
APP-4: [INFO] Validation report for ./Valid/Simple.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
VALID, no errors, no warnings and 1 info message found.
```

The next example shows the output generated when using the vanilla validator on a single invalid ODF spreadsheet package:

```text
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

```text
$ odf-validator ./PKG_7/PreviewImageMissing.ods
APP-1: [INFO] Validating ./PKG_7/PreviewImageMissing.ods.
APP-4: [INFO] Validation report for ./PKG_7/PreviewImageMissing.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
PKG-7: Thumbnails/thumbnail.png [WARNING] An OpenDocument Package SHOULD contain a preview image Thumbnails/thumbnail.png.
VALID, no errors, 1 warnings found and 1 info messages.
```

### Validating a single file using the Preservation Specification profile

The example below shows the output generated when using the preservation profile validator on a single valid ODF spreadsheet package, note the use of the `--profile` flag:

```text
$ odf-validator --profile ./Valid/Simple.ods
APP-1: [INFO] Validating ./Valid/Simple.ods.
APP-5: [INFO] DNA ODF Spreadsheets Preservation Specification Profile report for ./Valid/Simple.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
VALID, no errors, no warnings and 1 info message found.
```

Here's a file that fails one of the profile checks:

```text
$ odf-validator --profile ./ODF_7/WithZeroCellData.ods
APP-1: [INFO] Validating ./ODF_7/WithZeroCellData.ods. 
APP-5: [INFO] DNA ODF Spreadsheets Preservation Specification Profile report for ./ODF_7/WithZeroCellData.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
POL-7: content.xml [ERROR] Content check | The file MUST have values or objects in at least one cell.
NOT VALID, 1 errors, 0 warnings and 1 info messages.
```

Finally and example of a Preservation Profile rule that simply reports the presesnce of embedded objects:

```text
$ odf-validator --profile ./ODF_6/WithTwoEmbeddedWordDocument.ods
APP-1: [INFO] Validating ./ODF_6/WithTwoEmbeddedWordDocument.ods.
APP-5: [INFO] DNA ODF Spreadsheets Preservation Specification Profile report for ./ODF_6/WithTwoEmbeddedWordDocument.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
POL-6: content.xml [INFO] Embedded Objects | The spreadsheet MAY reference Embedded OpenDocument objects.
POL-6: content.xml [INFO] Embedded Objects | The spreadsheet MAY reference OLE objects.
VALID, no errors, no warnings and 3 info message found.
```
