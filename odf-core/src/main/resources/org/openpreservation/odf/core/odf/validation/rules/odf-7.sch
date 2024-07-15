<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
  <ns prefix="office" uri="urn:oasis:names:tc:opendocument:xmlns:office:1.0" />
  <ns prefix="table" uri="urn:oasis:names:tc:opendocument:xmlns:table:1.0" />
  <pattern id="POL_7">
    <title>Content check</title>
    <rule context="//office:spreadsheet">
      <assert id="POL_7.1" role="WARNING" test="count(table:table/table:table-row/table:table-cell[@office:value-type]) > 0 or count(table:table/table:table-row/table:covered-table-cell[@office:value-type]) > 0">The file MUST have values or objects in at least one cell.</assert>
    </rule>
  </pattern>
</schema>
