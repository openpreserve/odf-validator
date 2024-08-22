package org.openpreservation.odf.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.ValidationResults;
import org.openpreservation.format.xml.XmlTestUtils;
import org.openpreservation.odf.fmt.TestFiles;
import org.openpreservation.odf.xml.Metadata;
import org.openpreservation.odf.xml.Metadata.UserDefinedField;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.xml.sax.SAXException;

final class Utils {
    ParseResult parseResult = ValidationResults.parseResultOf(true, XmlTestUtils.exampleNamespace,
            new ArrayList<>(), new ArrayList<>(),
            "prefix", "name", new ArrayList<>(), new ArrayList<>());
    Map<String, String> stringValues = new HashMap<>();
    List<UserDefinedField> userDefinedFields = new ArrayList<>();
    Metadata metadata = OdfXmlDocuments.metadataOf("", stringValues, userDefinedFields);
    OdfXmlDocument odfDocument = OdfXmlDocuments
            .xmlDocumentFrom(TestFiles.EMPTY_FODS.openStream());

    public Utils() throws IOException, ParserConfigurationException, SAXException {
    }
}
