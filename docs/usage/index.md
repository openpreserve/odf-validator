# Usage

## Command line options

The command line options are currently as follows:

```shell
$ odf-validator --help
Usage: odf-validator [-hpV] [--format=<format>] FILE...
Validates Open Document Format spreadsheets.
      FILE...             A list of Open Document Format spreadsheet files to
                            validate.
      --format=<format>   Output results as TEXT, JSON or XML.
  -h, --help              Show this help message and exit.
  -p, --profile           Validate using extended Spreadsheet preservation
                            profile.
  -V, --version           Print version information and exit.
```

## Examples

### Validating a single file using the OpenDocument Format specification

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

### Validating a single file using the Preservation Specification profile

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
POL-7: content.xml [ERROR] Content check | The file MUST have values or objects in at least one cell.
NOT VALID, 1 errors, 0 warnings and 1 info messages.
```

Finally and example of a Preservation Profile rule that simply reports the presesnce of embedded objects:

```bash
$ odf-validator --profile ./ODF_6/WithTwoEmbeddedWordDocument.ods
APP-1: [INFO] Validating ./ODF_6/WithTwoEmbeddedWordDocument.ods.
APP-5: [INFO] DNA ODF Spreadsheets Preservation Specification Profile report for ./ODF_6/WithTwoEmbeddedWordDocument.ods.
DOC-3: mimetype [INFO] OpenDocument MIMETYPE application/vnd.oasis.opendocument.spreadsheet detected
POL-6: content.xml [INFO] Embedded Objects | The spreadsheet MAY reference Embedded OpenDocument objects.
POL-6: content.xml [INFO] Embedded Objects | The spreadsheet MAY reference OLE objects.
VALID, no errors, no warnings and 3 info message found.
```
