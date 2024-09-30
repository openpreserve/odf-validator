package org.openpreservation.odf.validation.rules;

import java.util.Objects;

import org.openpreservation.messages.Message.Severity;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.validation.ValidationReport;

abstract class AbstractRule implements Rule {
    final String id;
    final String name;
    final String description;
    final Severity severity;
    final Boolean isPrerequisite;

    AbstractRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.severity = severity;
        this.isPrerequisite = Boolean.valueOf(isPrerequisite);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Severity getSeverity() {
        return this.severity;
    }

    @Override
    public boolean isPrerequisite() {
        return this.isPrerequisite.booleanValue();
    }

    @Override
    public MessageLog check(ValidationReport report) throws ParseException {
        return report.document.isPackage() ? check(report.document.getPackage()) : check(report.document.getDocument().getXmlDocument());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, name, description, severity, isPrerequisite);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AbstractRule))
            return false;
        final AbstractRule other = (AbstractRule) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(description, other.description)
                && Objects.equals(severity, other.severity)
                && Objects.equals(isPrerequisite, other.isPrerequisite);
    }
}
