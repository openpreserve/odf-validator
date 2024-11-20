package org.openpreservation.odf.validation.rules;

import java.util.Collections;
import java.util.Set;

import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Profile;
import org.openpreservation.odf.validation.ProfileResult;
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

    @Override
    public ProfileResult check(OpenDocument document) throws ParseException {
        MessageLog log = Messages.messageLogInstance();
        for (Rule rule : this.rules) {
            log.add(rule.check(document).getMessages());
        }
        Collections.singletonMap(document.getDocument().getXmlDocument().getRootName(), log);
        return null;
    }
}
