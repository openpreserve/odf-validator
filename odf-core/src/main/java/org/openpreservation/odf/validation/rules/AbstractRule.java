package org.openpreservation.odf.validation.rules;

import org.openpreservation.odf.validation.Rule;

abstract class AbstractRule implements Rule {
    final String id;
    final String name;
    final String description;

    AbstractRule(final String id, final String name, final String description) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
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
}
