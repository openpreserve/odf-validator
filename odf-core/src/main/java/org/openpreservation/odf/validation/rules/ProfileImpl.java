package org.openpreservation.odf.validation.rules;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.messages.Message;
import org.openpreservation.messages.MessageLog;
import org.openpreservation.messages.Messages;
import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.validation.ValidatingParser;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.ValidationReportImpl;
import org.openpreservation.odf.validation.ValidationResult;
import org.openpreservation.odf.validation.Validator;
import org.openpreservation.odf.validation.Validators;
import org.xml.sax.SAXException;

final class ProfileImpl extends AbstractProfile {
    private final ValidatingParser validatingParser = Validators.getValidatingParser();

    static final ProfileImpl of(final String id, final String name, final String description, final Set<Rule> rules)
            throws ParserConfigurationException, SAXException {
        return new ProfileImpl(id, name, description, rules);
    }

    private ProfileImpl(final String id, final String name, final String description, final Set<Rule> rules)
            throws ParserConfigurationException, SAXException {
        super(id, name, description, rules);
    }

    @Override
    public ValidationReport check(final OpenDocument document) throws ParseException {
        Objects.requireNonNull(document, "document must not be null");
        try {
            final MessageLog messages = Messages.messageLogInstance();
            ValidationResult result = document.isPackage()
                    ? this.validatingParser.validatePackage(document.getPackage())
                    : new Validator().validateOpenDocument(document);
             messages.add(getRulesetMessages(document,
                    this.rules.stream().filter(Rule::isPrerequisite).collect(Collectors.toList())));
            if (!messages.hasErrors()) {
                messages.add(getRulesetMessages(document,
                        this.rules.stream().filter(rule -> !rule.isPrerequisite()).collect(Collectors.toList())));
            }
            final String packageName = document == null || document.getPackage() == null ? ""
                    : document.getPackage().getName();
            return ValidationReportImpl.of((document.getDocument() != null) ? document.getDocument().getMetadata() : null,
                                           (document.getPackage() != null) ? document.getPackage().getManifest() : null,
                                           result, Validators.profileResultOf(packageName, this.name, messages));
        } catch (FileNotFoundException e) {
            throw new ParseException("File not found exception when processing package.", e);
        } catch (IOException e) {
            throw new ParseException("IO exception when processing package.", e);
        }
    }

    private final Map<String, List<Message>> getRulesetMessages(final OpenDocument document,
            final Collection<Rule> rules) throws ParseException {
        final MessageLog messages = Messages.messageLogInstance();
        for (final Rule rule : rules) {
            final MessageLog ruleMessages = rule.check(document);
            messages.add(ruleMessages.getMessages());
        }
        return messages.getMessages();
    }
}
