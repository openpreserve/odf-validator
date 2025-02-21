<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
  <ns prefix="office" uri="urn:oasis:names:tc:opendocument:xmlns:office:1.0" />
  <ns prefix="table" uri="urn:oasis:names:tc:opendocument:xmlns:table:1.0" />
  <ns prefix="draw" uri="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0" />
  <pattern id="POL-6">
    <title>Embedded objects check</title>
    <rule context="//office:document-content">
      <assert id="POL-6.1" role="INFO" test="count(//draw:object) &lt; 1">The spreadsheet MAY reference Embedded OpenDocument objects.</assert>
      <assert id="POL-6.2" role="INFO" test="count(//draw:object-ole) &lt; 1">The spreadsheet MAY reference OLE objects.</assert>
    </rule>
  </pattern>
</schema>