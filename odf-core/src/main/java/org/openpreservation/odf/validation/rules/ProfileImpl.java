package org.openpreservation.odf.validation.rules;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.ProfileResult;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.validation.ValidationReport;

final class ProfileImpl extends AbstractProfile {
    private ValidationReport report = null;

    static final ProfileImpl of(final String id, final String name, final String description, final Set<Rule> rules) {
        return new ProfileImpl(id, name, description, rules);
    }

    private ProfileImpl(final String id, final String name, final String description, final Set<Rule> rules) {
        super(id, name, description, rules);
    }

    @Override
    public ProfileResult check(final OdfPackage odfPackage) throws ParseException {
        final MessageLog messages = Messages.messageLogInstance();
        messages.add(getRulesetMessages(odfPackage,
                this.rules.stream().filter(Rule::isPrerequisite).collect(Collectors.toList())));
        if (!messages.hasErrors()) {
            messages.add(getRulesetMessages(odfPackage,
                    this.rules.stream().filter(rule -> !rule.isPrerequisite()).collect(Collectors.toList())));
        }
        return ProfileResultImpl.of(odfPackage.getName(), report, messages);
    }

    private final Map<String, List<Message>> getRulesetMessages(final OdfPackage odfPackage,
            final Collection<Rule> rules) throws ParseException {
        final MessageLog messages = Messages.messageLogInstance();
        for (final Rule rule : rules) {
            final MessageLog ruleMessages = rule.check(odfPackage);
            if (rule instanceof ValidPackageRule) {
                report = ((ValidPackageRule) rule).getValidationReport();
            }
            messages.add(ruleMessages.getMessages());
        }
        return messages.getMessages();
    }
}
