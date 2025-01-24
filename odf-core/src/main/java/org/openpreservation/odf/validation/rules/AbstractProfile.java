package org.openpreservation.odf.validation.rules;

import java.util.Collections;
import java.util.Set;

import org.openpreservation.odf.validation.Profile;
import org.openpreservation.odf.validation.Rule;

abstract class AbstractProfile implements Profile {
    final String id;
    final String name;
    final String description;
    final Set<Rule> rules;

    AbstractProfile(final String id, final String name, final String description, final Set<Rule> rules) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.rules = Collections.unmodifiableSet(rules);
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
    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(this.rules);
    }
}
