<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
  <ns prefix="office" uri="urn:oasis:names:tc:opendocument:xmlns:office:1.0" />
  <ns prefix="table" uri="urn:oasis:names:tc:opendocument:xmlns:table:1.0" />
  <ns prefix="form" uri="urn:oasis:names:tc:opendocument:xmlns:form:1.0" />
  <ns prefix="text" uri="urn:oasis:names:tc:opendocument:xmlns:text:1.0" />
  <pattern id="POL_5">
    <title>External data check</title>
    <rule context="//office:document-content">
      <assert id="POL_5.1" role="ERROR" test="count(//table:dde-links) &lt; 1">Table Dynamic Data Exchange link declarations detected.</assert>
      <assert id="POL_5.2" role="ERROR" test="count(//office:dde-source) &lt; 1">Dynamic Data Exchange connection detected.</assert>
      <assert id="POL_5.3" role="ERROR" test="count(//table:table-cell[@table:formula]) &lt; 1">Table formula detected.</assert>
      <assert id="POL_5.4" role="ERROR" test="count(//form:connection-resource) &lt; 1">Form connection resource detected.</assert>
      <assert id="POL_5.5" role="ERROR" test="count(//table:table-source) &lt; 1">Table source detected.</assert>
      <assert id="POL_5.6" role="ERROR" test="count(//table:cell-range-source) &lt; 1">Table cell-range source detected.</assert>
      <assert id="POL_5.7" role="ERROR" test="count(//table:database-source-sql) &lt; 1">SQL database source detected.</assert>
      <assert id="POL_5.8" role="ERROR" test="count(//table:database-source-table) &lt; 1">Table database source detected.</assert>
      <assert id="POL_5.9" role="ERROR" test="count(//table:database-source-query) &lt; 1">Query database source detected.</assert>
      <assert id="POL_5.10" role="ERROR" test="count(//text:dde-connection-decls) &lt; 1">Dynamic Data Exchange declarations detected.</assert>
    </rule>
  </pattern>
</schema>
