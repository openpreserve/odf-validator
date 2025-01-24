package org.openpreservation.odf.validation.rules;

import java.util.Objects;

import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.PackageParser.ParseException;

public class EmbeddedObjectsRule extends AbstractRule {
    static final String ODF6_SCHEMATRON = "org/openpreservation/odf/core/odf/validation/rules/odf-6.sch";

    static final EmbeddedObjectsRule getInstance(final Severity severity) {
        return new EmbeddedObjectsRule("POL-6", "Embedded Objects",
                "Embedded files MAY be present in the ODS file.", severity, false);
    }

    final SchematronRule schematron;

    private EmbeddedObjectsRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) {
        super(id, name, description, severity, isPrerequisite);
        this.schematron = SchematronRule.getInstance(id, name,
                description, severity, isPrerequisite,
                ClassLoader.getSystemResource(ODF6_SCHEMATRON));
    }

    @Override
    public MessageLog check(final OpenDocument document) throws ParseException {
        Objects.requireNonNull(document, "document must not be null");
        return this.schematron.check(document);
    }

}
