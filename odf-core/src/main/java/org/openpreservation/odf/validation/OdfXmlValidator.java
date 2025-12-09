package org.openpreservation.odf.validation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.Schema;

import org.openpreservation.format.xml.ParseResult;
import org.openpreservation.format.xml.XmlValidator;
import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.messages.MessageFactory;
import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Parameter.ParameterList;
import org.openpreservation.odf.xml.OdfNamespaces;
import org.openpreservation.odf.xml.OdfSchemaFactory;
import org.openpreservation.odf.xml.OdfXmlDocument;
import org.openpreservation.odf.xml.OdfXmlDocuments;
import org.openpreservation.odf.xml.Version;

public class OdfXmlValidator {
    private static final MessageFactory FACTORY = Messages.getInstance();
    private final XmlValidator validator = new XmlValidator();
    private final boolean isExtended;

    static OdfXmlValidator getInstance(final boolean isExtended) {
        return new OdfXmlValidator(isExtended);
    }
  
    private OdfXmlValidator(final boolean isExtended) {
        super();
        this.isExtended = isExtended;
    }

    public static List<Message> getDocInfoMessages(final OdfXmlDocument doc) {
        List<Message> messages = new ArrayList<>();
        if (doc.getParseResult().isWellFormed() && doc.getParseResult().isRootName("office:document")) {
            messages.add(FACTORY.getInfo("DOC-2",
                    Messages.parameterListInstance().add("version", doc.getVersion().version)));
            if (doc.getFormat().isPackage()) {
                messages.add(FACTORY.getInfo("DOC-3",
                        Messages.parameterListInstance().add("mimetype", doc.getFormat().mime)));
            } else {
                messages.add(FACTORY.getError("DOC-4",
                        Messages.parameterListInstance().add("mimetype", doc.getFormat().mime)));
            }
        }
        return messages;
    }

    public List<Message> validate(final Path toValidate, ParseResult parseResult) throws IOException {
        List<Message> messages = new ArrayList<>();
        ParseResult result = parseResult;
        if (result.isWellFormed()) {
            Version version = Version.ODF_13;
            final OdfXmlDocument doc = OdfXmlDocuments.odfXmlDocumentOf(result);
            if (doc.isExtended()) {
                ParameterList params = Messages.parameterListInstance().add("namespaces",
                        Utils.collectNsPrefixes(OdfXmlDocuments.odfXmlDocumentOf(result)
                                .getForeignNamespaces()));
                messages.add(FACTORY.getMessage("DOC-8", this.isExtended ? Severity.INFO : Severity.ERROR, params));
            }
            if (this.isExtended || !doc.isExtended()) {
                final Schema schema = new OdfSchemaFactory().getSchema(OdfNamespaces.OFFICE, version);
                result = validator.validate(parseResult, Files.newInputStream(toValidate), schema);
            }
        } else {
            messages.add(FACTORY.getError("DOC-1"));
        }
        messages.addAll(result.getMessages());
        return messages;
    }
}
