package org.openpreservation.odf.validation.rules;

import java.util.Objects;

import org.openpreservation.odf.validation.Rule;

abstract class AbstractRule implements Rule {
    final String id;
    final String name;
    final String description;
    final Boolean isPrerequisite;

    AbstractRule(final String id, final String name, final String description, final boolean isPrerequisite) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
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
    public boolean isPrerequisite() {
        return this.isPrerequisite.booleanValue();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, name, description, isPrerequisite);
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
                && Objects.equals(isPrerequisite, other.isPrerequisite);
    }
}
