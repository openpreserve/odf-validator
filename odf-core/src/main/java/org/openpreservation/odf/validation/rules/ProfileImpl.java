package org.openpreservation.odf.validation.rules;

import java.io.IOException;
import java.util.SortedSet;

import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.validation.ProfileResult;
import org.openpreservation.odf.validation.Rule;

final class ProfileImpl extends AbstractProfile {
    private ProfileImpl(final String id, final String name, final String description, final SortedSet<Rule> rules) {
        super(id, name, description, rules);
    }

    @Override
    public ProfileResult check(final OdfPackage odfPackage) throws IOException {
        for (final Rule rule : this.rules) {
            rule.check(odfPackage);
        }
        return null;
    }
 
    static final ProfileImpl of(final String id, final String name, final String description, final SortedSet<Rule> rules) {
        return new ProfileImpl(id, name, description, rules);
    }
}
