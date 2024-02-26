# ODF Policy Checks

The ODF Spreadsheet Validator checks the following policies:

## POL_1 Encryption

The file MUST NOT be encrypted, either using a password or a GPG key.

This is implemented using a check for encryption information in the package manifest [Section 4.4 of the Odf Package Sepcification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#__RefHeading__752833_826425813). It does not check for encrypted zip entries [Section 3.4.1 of the ODF Package Specification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#a_3_4_1_General)

## POL_2 Standard compliance

The file MUST comply with the standard “OASIS Open Document Format for Office Applications (OpenDocument) v1.3”. Note that an ODF 1.3 extended package is not permitted.

This is enforced by running the standard validation checks see [Validation Checks](./validation/).

## POL_3 Package mimetype entry

An ODF package MUST have a mimetype entry as specified in the Section 3.3 of the ODF specification v1.3. A MIME type declaration is required in the first line of the file "mimetype" and an entry in the file "\META-INF\manifest.xml"

## POL_4 Extension & MIME type

The file MUST have the matching extension and MIME type pairs. MIME type: application/vnd.oasis.opendocument.spreadsheet", Extension: ".ods".

## POL_5 External data

The package MAY have any references to external data. This includes data connections, RealTimeData functions, cell formula references and objects. Any external data references such as data connections, RTD functions, cell references or objects MAY be removed, however the calculated cell value MUST be preserved.

The policy checker tests for the following XML elements:

* `table:dde-links` Table Dynamic Data Exchange links
* `office:dde-source` Dynamic Data Exchange connection
* `table:table-cell[@table:formula])` &lt; 1">Table formula
* `form:connection-resource` Form connection resource
* `table:table-source` Table source
* `table:cell-range-source` Table cell-range source
* `table:database-source-sql` SQL database source
* `table:database-source-table` Table database source
* `table:database-source-query` Query database source
* `text:dde-connection-decls` Dynamic Data Exchange declaration

## POL_6 Embedded objects

Embedded files MAY be present in the the spreadsheet OpenDocument package.

The policy checker tests for the following XML elements which represent embedded objects in the spreadsheet package:

* `draw:object` Draw object
* `draw:object-ole` Draw OLE object

## POL_7 Content

The package MUST have values or objects in at least one cell. It is quite possible that some cell values in a spreadsheet are missing. This is not considered an error.

The policy checker tests for the following XML elements:

* `table:table/table:table-row/table:table-cell[@office:value-type]` Table cells with a value type
* `table:table/table:table-row/table:covered-table-cell[@office:value-type]` Covered table cells with a value type

## POL_8 Macros

The package MUST NOT contain any macros.

The policy checker tests for the following XML elements:

* `script:event-listener` Macro event listener

## POL_9 Signatures

The package MUST NOT contain any digital signatures.

This is implemented as a test for files with names containg the term "signatures" below the package `META-INF` directory. [Section 3.5 of the Odf Package Sepcification](https://docs.oasis-open.org/office/OpenDocument/v1.3/os/part2-packages/OpenDocument-v1.3-os-part2-packages.html#__RefHeading__752817_826425813)
