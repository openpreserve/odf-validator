<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
  <ns prefix="office" uri="urn:oasis:names:tc:opendocument:xmlns:office:1.0" />
  <ns prefix="table" uri="urn:oasis:names:tc:opendocument:xmlns:table:1.0" />
  <ns prefix="form" uri="urn:oasis:names:tc:opendocument:xmlns:form:1.0" />
  <ns prefix="text" uri="urn:oasis:names:tc:opendocument:xmlns:text:1.0" />
  <pattern id="POL-5">
    <title>External data check</title>
    <rule context="//office:document-content">
      <assert id="POL-5.1" role="INFO" test="count(//table:dde-links) &lt; 1">Table Dynamic Data Exchange link declarations detected.</assert>
      <assert id="POL-5.2" role="INFO" test="count(//office:dde-source) &lt; 1">Dynamic Data Exchange connection detected.</assert>
      <assert id="POL-5.3" role="INFO" test="count(//table:table-cell[@table:formula]) &lt; 1">Table formula detected.</assert>
      <assert id="POL-5.4" role="INFO" test="count(//form:connection-resource) &lt; 1">Form connection resource detected.</assert>
      <assert id="POL-5.5" role="INFO" test="count(//table:table-source) &lt; 1">Table source detected.</assert>
      <assert id="POL-5.6" role="INFO" test="count(//table:cell-range-source) &lt; 1">Table cell-range source detected.</assert>
      <assert id="POL-5.7" role="INFO" test="count(//table:database-source-sql) &lt; 1">SQL database source detected.</assert>
      <assert id="POL-5.8" role="INFO" test="count(//table:database-source-table) &lt; 1">Table database source detected.</assert>
      <assert id="POL-5.9" role="INFO" test="count(//table:database-source-query) &lt; 1">Query database source detected.</assert>
      <assert id="POL-5.10" role="INFO" test="count(//text:dde-connection-decls) &lt; 1">Dynamic Data Exchange declarations detected.</assert>
    </rule>
  </pattern>
</schema>
