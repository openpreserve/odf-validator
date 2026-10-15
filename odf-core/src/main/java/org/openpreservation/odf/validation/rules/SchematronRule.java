package org.openpreservation.odf.validation.rules;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.openpreservation.odf.document.OpenDocument;
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser.ParseException;
import org.openpreservation.odf.validation.messages.Message.Severity;
import org.openpreservation.odf.validation.messages.MessageLog;
import org.openpreservation.odf.validation.messages.Messages;
import org.openpreservation.odf.validation.messages.Parameter.ParameterList;

/**
 * Evaluates the asserts of a Schematron file in a single StAX pass instead of building a DOM of each XML entry.
 * Only the subset used by odf-5.sch to odf-8.sch is supported; anything else fails when the rule is loaded.
 */
final class SchematronRule extends AbstractRule {
    private static final String NS_SCH = "http://purl.oclc.org/dsdl/schematron";

    // count(path) operator number, combined with "or" / "and" (no parentheses, "and" binds tighter).
    private static final Pattern COMPARISON = Pattern.compile(
            "count\\(\\s*([^()]+?)\\s*\\)\\s*(<=|>=|!=|<|>|=)\\s*(\\d+)");
    private static final Pattern STEP = Pattern.compile(
            "([\\w.-]+:[\\w.-]+)(?:\\[@([\\w.-]+:[\\w.-]+)\\])?");

    /** A path step: element name plus an optional required attribute. */
    private static final class Step {
        final QName element;
        final QName requiredAttribute;

        Step(final QName element, final QName requiredAttribute) {
            this.element = element;
            this.requiredAttribute = requiredAttribute;
        }

        boolean matches(final QName qName, final XMLStreamReader reader) {
            return this.element.equals(qName) && (this.requiredAttribute == null
                    || reader.getAttributeValue(this.requiredAttribute.getNamespaceURI(),
                            this.requiredAttribute.getLocalPart()) != null);
        }
    }

    /** count(path): absolute ("//x", whole document, rule == null) or relative to the context node ("a/b/c"). */
    private static final class Term {
        final SchRule rule;
        final Step[] steps;
        final int index;
        // Beyond this count no comparison changes its result, so counting stops.
        long cap;

        Term(final SchRule rule, final Step[] steps, final int index) {
            this.rule = rule;
            this.steps = steps;
            this.index = index;
        }

        Step last() {
            return this.steps[this.steps.length - 1];
        }
    }

    private static final class Comparison {
        final Term term;
        final String operator;
        final long value;

        Comparison(final Term term, final String operator, final long value) {
            this.term = term;
            this.operator = operator;
            this.value = value;
        }

        boolean holds(final long count) {
            switch (this.operator) {
                case "<": return count < this.value;
                case "<=": return count <= this.value;
                case ">": return count > this.value;
                case ">=": return count >= this.value;
                case "=": return count == this.value;
                default: return count != this.value;
            }
        }
    }

    private static final class Assert {
        final List<List<Comparison>> anyOf = new ArrayList<>();
        Severity role;
        String text;
    }

    private static final class SchRule {
        final QName context;
        final List<Assert> asserts = new ArrayList<>();
        final List<Term> relativeTerms = new ArrayList<>();

        SchRule(final QName context) {
            this.context = context;
        }
    }

    /** One occurrence of a context element with the counts of its relative paths. */
    private static final class ContextHit {
        final SchRule rule;
        final long[] counts;

        ContextHit(final SchRule rule) {
            this.rule = rule;
            this.counts = new long[rule.relativeTerms.size()];
        }
    }

    static final SchematronRule getInstance(final String id, final String name, final String description,
            final Severity severity, final boolean isPrerequisite, final URL schematronLoc) {
        return new SchematronRule(id, name, description, severity, isPrerequisite, schematronLoc);
    }

    private final List<SchRule> rules = new ArrayList<>();
    private final List<Term> absoluteTerms = new ArrayList<>();
    // Terms and contexts keyed by element name: one lookup per element instead of a loop.
    private final Map<QName, List<Term>> absoluteByElement = new HashMap<>();
    private final Map<QName, List<Term>> relativeByElement = new HashMap<>();
    private final Map<QName, List<SchRule>> rulesByContext = new HashMap<>();
    private final XMLInputFactory factory;

    private SchematronRule(final String id, final String name, final String description, final Severity severity,
            final boolean isPrerequisite, final URL schematronLoc) {
        super(id, name, description, severity, isPrerequisite);
        Objects.requireNonNull(schematronLoc, "schematronLoc must not be null");
        this.factory = XMLInputFactory.newFactory();
        this.factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        this.factory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        this.factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        try (InputStream is = schematronLoc.openStream()) {
            load(is);
        } catch (IOException | XMLStreamException e) {
            throw new IllegalStateException("Schematron " + schematronLoc + " could not be loaded.", e);
        }
        if (this.rules.isEmpty()) {
            throw new IllegalStateException("Schematron " + schematronLoc + " contains no rules.");
        }
        for (final Term term : this.absoluteTerms) {
            this.absoluteByElement.computeIfAbsent(term.last().element, k -> new ArrayList<>()).add(term);
        }
        for (final SchRule rule : this.rules) {
            this.rulesByContext.computeIfAbsent(rule.context, k -> new ArrayList<>()).add(rule);
            for (final Term term : rule.relativeTerms) {
                this.relativeByElement.computeIfAbsent(term.last().element, k -> new ArrayList<>()).add(term);
            }
        }
    }

    private void load(final InputStream is) throws XMLStreamException {
        final Map<String, String> namespaces = new HashMap<>();
        final XMLStreamReader reader = this.factory.createXMLStreamReader(is);
        try {
            SchRule rule = null;
            while (reader.hasNext()) {
                if (reader.next() != XMLStreamConstants.START_ELEMENT
                        || !NS_SCH.equals(reader.getNamespaceURI())) {
                    continue;
                }
                switch (reader.getLocalName()) {
                    case "ns":
                        namespaces.put(reader.getAttributeValue(null, "prefix"), reader.getAttributeValue(null, "uri"));
                        break;
                    case "rule":
                        rule = new SchRule(parseContext(reader.getAttributeValue(null, "context"), namespaces));
                        this.rules.add(rule);
                        break;
                    case "assert":
                        if (rule == null) {
                            throw new IllegalArgumentException("assert outside of a rule");
                        }
                        final Assert a = parseTest(reader.getAttributeValue(null, "test"), rule, namespaces);
                        final String role = reader.getAttributeValue(null, "role");
                        a.role = role == null ? this.severity : Severity.valueOf(role.trim().toUpperCase());
                        a.text = reader.getElementText().trim().replaceAll("\\s+", " ");
                        rule.asserts.add(a);
                        break;
                    case "report":
                    case "let":
                    case "include":
                    case "extends":
                        throw new IllegalArgumentException("Unsupported Schematron element: "
                                + reader.getLocalName());
                    default:
                        break;
                }
            }
        } finally {
            reader.close();
        }
    }

    private static QName parseContext(final String context, final Map<String, String> namespaces) {
        if (context == null || !context.trim().startsWith("//")) {
            throw new IllegalArgumentException("Unsupported rule context: " + context);
        }
        final Step[] steps = parseSteps(context.trim().substring(2), namespaces);
        if (steps.length != 1 || steps[0].requiredAttribute != null) {
            throw new IllegalArgumentException("Unsupported rule context: " + context);
        }
        return steps[0].element;
    }

    private Assert parseTest(final String test, final SchRule rule, final Map<String, String> namespaces) {
        final Assert a = new Assert();
        for (final String disjunct : test.trim().split("\\s+or\\s+")) {
            final List<Comparison> allOf = new ArrayList<>();
            for (final String conjunct : disjunct.trim().split("\\s+and\\s+")) {
                final Matcher m = COMPARISON.matcher(conjunct.trim());
                if (!m.matches()) {
                    throw new IllegalArgumentException("Unsupported assert test: " + test);
                }
                final Term term = parseTerm(m.group(1), rule, namespaces);
                final long value = Long.parseLong(m.group(3));
                term.cap = Math.max(term.cap, value + 1);
                allOf.add(new Comparison(term, m.group(2), value));
            }
            a.anyOf.add(allOf);
        }
        return a;
    }

    private Term parseTerm(final String path, final SchRule rule, final Map<String, String> namespaces) {
        final boolean absolute = path.startsWith("//");
        final Step[] steps = parseSteps(absolute ? path.substring(2) : path, namespaces);
        if (absolute && steps.length != 1) {
            throw new IllegalArgumentException("Only single step // paths are supported: " + path);
        }
        for (int i = 0; i < steps.length - 1; i++) {
            if (steps[i].requiredAttribute != null) {
                throw new IllegalArgumentException("Predicates are only supported on the last step: " + path);
            }
        }
        final List<Term> terms = absolute ? this.absoluteTerms : rule.relativeTerms;
        final Term term = new Term(absolute ? null : rule, steps, terms.size());
        terms.add(term);
        return term;
    }

    private static Step[] parseSteps(final String path, final Map<String, String> namespaces) {
        final String[] parts = path.trim().split("/");
        final Step[] steps = new Step[parts.length];
        for (int i = 0; i < parts.length; i++) {
            final Matcher m = STEP.matcher(parts[i].trim());
            if (!m.matches()) {
                throw new IllegalArgumentException("Unsupported path step: " + parts[i]);
            }
            steps[i] = new Step(qName(m.group(1), namespaces),
                    m.group(2) == null ? null : qName(m.group(2), namespaces));
        }
        return steps;
    }

    private static QName qName(final String prefixed, final Map<String, String> namespaces) {
        final int colon = prefixed.indexOf(':');
        final String uri = namespaces.get(prefixed.substring(0, colon));
        if (uri == null) {
            throw new IllegalArgumentException("Unknown prefix: " + prefixed);
        }
        return new QName(uri, prefixed.substring(colon + 1));
    }

    @Override
    public MessageLog check(final OpenDocument document) throws ParseException {
        Objects.requireNonNull(document, "document must not be null");
        if (document.isPackage()) {
            return this.check(document.getPackage());
        }
        try (final InputStream is = new FileInputStream(document.getPath().toFile())) {
            return check(document.getPath().toString(), is);
        } catch (IOException e) {
            throw new ParseException("Unexpected Exception caught when executing Schematron checks.", e);
        }
    }

    public MessageLog check(final OdfPackage odfPackage) throws ParseException {
        final MessageLog messageLog = Messages.messageLogInstance();
        for (final FileEntry entry : odfPackage.getXmlEntries()) {
            if (!OdfPackages.isOdfXml(entry.getFullPath()) || entry.isEncrypted()) {
                continue;
            }
            try (InputStream is = odfPackage.getEntryStream(entry)) {
                messageLog.add(check(entry.getFullPath(), is).getMessages());
            } catch (IOException e) {
                // No need to catch stream close exception
            }
        }
        return messageLog;
    }

    private MessageLog check(final String entryName, final InputStream is) throws ParseException {
        final List<ContextHit> hits = new ArrayList<>();
        final long[] absoluteCounts = new long[this.absoluteTerms.size()];
        try {
            scan(is, hits, absoluteCounts);
        } catch (XMLStreamException e) {
            // Ignore unparsable entries, they are reported by the package rules.
            return Messages.messageLogInstance();
        } catch (RuntimeException e) {
            throw new ParseException("Unexpected Exception caught when executing Schematron checks.", e);
        }
        final MessageLog messageLog = Messages.messageLogInstance();
        final ParameterList parameters = Messages.parameterListInstance().add("entryPath", entryName);
        // Same order as Schematron: per rule the context nodes in document order, per node its asserts.
        for (final SchRule rule : this.rules) {
            for (final ContextHit hit : hits) {
                if (hit.rule != rule) {
                    continue;
                }
                for (final Assert a : rule.asserts) {
                    if (!holds(a, hit, absoluteCounts)) {
                        messageLog.add(entryName,
                                Messages.getMessageInstance(this.id, a.role, this.getName(), a.text, parameters));
                    }
                }
            }
        }
        return messageLog;
    }

    private static boolean holds(final Assert a, final ContextHit hit, final long[] absoluteCounts) {
        for (final List<Comparison> allOf : a.anyOf) {
            boolean all = true;
            for (final Comparison c : allOf) {
                final long count = c.term.rule == null ? absoluteCounts[c.term.index] : hit.counts[c.term.index];
                if (!c.holds(count)) {
                    all = false;
                    break;
                }
            }
            if (all) {
                return true;
            }
        }
        return false;
    }

    private void scan(final InputStream is, final List<ContextHit> hits, final long[] absoluteCounts)
            throws XMLStreamException {
        // Per open element: its name and the context hits starting there (for relative paths).
        final Deque<QName> path = new ArrayDeque<>();
        final Deque<List<ContextHit>> openHits = new ArrayDeque<>();
        final XMLStreamReader reader = this.factory.createXMLStreamReader(is);
        try {
            while (reader.hasNext()) {
                final int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    final QName qName = reader.getName();
                    final List<Term> absolute = this.absoluteByElement.get(qName);
                    if (absolute != null) {
                        for (final Term term : absolute) {
                            if (absoluteCounts[term.index] < term.cap && term.last().matches(qName, reader)) {
                                absoluteCounts[term.index]++;
                            }
                        }
                    }
                    final List<Term> relative = this.relativeByElement.get(qName);
                    if (relative != null) {
                        countRelative(relative, reader, path, openHits);
                    }
                    List<ContextHit> started = null;
                    final List<SchRule> contextRules = this.rulesByContext.get(qName);
                    if (contextRules != null) {
                        for (final SchRule rule : contextRules) {
                            final ContextHit hit = new ContextHit(rule);
                            hits.add(hit);
                            if (!rule.relativeTerms.isEmpty()) {
                                if (started == null) {
                                    started = new ArrayList<>(1);
                                }
                                started.add(hit);
                            }
                        }
                    }
                    path.push(qName);
                    openHits.push(started == null ? List.of() : started);
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    path.pop();
                    openHits.pop();
                }
            }
        } finally {
            reader.close();
        }
    }

    // Relative path s1/.../sn: the current element matches sn, its n-1 ancestors match s(n-1)..s1
    // and the n-th ancestor is a context node of the term's rule.
    private static void countRelative(final List<Term> terms, final XMLStreamReader reader,
            final Deque<QName> path, final Deque<List<ContextHit>> openHits) {
        for (final Term term : terms) {
            final int n = term.steps.length;
            if (path.size() < n || !term.last().matches(reader.getName(), reader)) {
                continue;
            }
            final Iterator<QName> names = path.iterator();
            final Iterator<List<ContextHit>> contexts = openHits.iterator();
            boolean ok = true;
            for (int i = n - 2; i >= 0 && ok; i--) {
                ok = term.steps[i].element.equals(names.next());
                contexts.next();
            }
            if (!ok) {
                continue;
            }
            for (final ContextHit hit : contexts.next()) {
                if (hit.rule == term.rule && hit.counts[term.index] < term.cap) {
                    hit.counts[term.index]++;
                }
            }
        }
    }
}
