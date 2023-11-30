package org.openpreservation.odf.validation.rules;

import java.io.IOException;
import java.util.Set;

import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.validation.ProfileResult;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.validation.ValidationReport;

final class ProfileImpl extends AbstractProfile {
    static final ProfileImpl of(final String id, final String name, final String description, final Set<Rule> rules) {
        return new ProfileImpl(id, name, description, rules);
    }

    private ProfileImpl(final String id, final String name, final String description, final Set<Rule> rules) {
        super(id, name, description, rules);
    }
 
    @Override
    public ProfileResult check(final OdfPackage odfPackage) throws IOException {
        ValidationReport report = null;
        final MessageLog messages = Messages.messageLogInstance();
        for (final Rule rule : this.rules) {
            if (!rule.isPrerequisite()) {
                continue;
            }
            final MessageLog ruleMessages = rule.check(odfPackage);
            if (rule instanceof ValidPackageRule) {
                report = ((ValidPackageRule) rule).getValidationReport();
            }
            if (ruleMessages.hasErrors()) {
                return ProfileResultImpl.of(odfPackage.getName(), report, ruleMessages);
            }
            messages.add(ruleMessages.getMessages());
        }
        for (final Rule rule : this.rules) {
            if (rule.isPrerequisite()) {
                continue;
            }
            final MessageLog ruleMessages = rule.check(odfPackage);
            messages.add(ruleMessages.getMessages());
        }
        return ProfileResultImpl.of(odfPackage.getName(), report, messages);
    }
}
