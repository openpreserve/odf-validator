<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
  <ns prefix="office" uri="urn:oasis:names:tc:opendocument:xmlns:office:1.0" />
  <ns prefix="table" uri="urn:oasis:names:tc:opendocument:xmlns:table:1.0" />
  <ns prefix="script" uri="urn:oasis:names:tc:opendocument:xmlns:script:1.0" />
  <pattern id="ODF_8">
    <title>Macro check</title>
    <rule context="//office:document-content">
      <assert id="ODF_8.1" role="ERROR" test="count(//script:event-listener) &lt; 1">The spreadsheet MUST NOT reference any macros.</assert>
    </rule>
  </pattern>
</schema>