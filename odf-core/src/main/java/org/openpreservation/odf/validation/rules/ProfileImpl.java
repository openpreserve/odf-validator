package org.openpreservation.odf.validation.rules;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.OdfValidators;
import org.openpreservation.odf.validation.Rule;
import org.openpreservation.odf.validation.ValidatingParser;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.ValidationReportImpl;
import org.openpreservation.odf.validation.ValidationResult;
import org.openpreservation.odf.validation.messages.Message;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.messages.Messages;
import org.xml.sax.SAXException;

final class ProfileImpl extends AbstractProfile {
    private final ValidatingParser validatingParser = OdfValidators.getValidatingParser();

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
                    : OdfValidators.getOdfValidator().validate(document);
             messages.add(getRulesetMessages(document,
                    this.rules.stream().filter(Rule::isPrerequisite).collect(Collectors.toList())));
            if (!messages.hasErrors()) {
                messages.add(getRulesetMessages(document,
                        this.rules.stream().filter(rule -> !rule.isPrerequisite()).collect(Collectors.toList())));
            }
            final String packageName = document == null || document.getPackage() == null ? ""
                    : document.getPackage().getName();
            return ValidationReportImpl.of(packageName,
                                           document,
                                           new ArrayList<>(Arrays.asList(result, OdfValidators.resultOf(name, messages))));
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
