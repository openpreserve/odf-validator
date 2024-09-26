package org.openpreservation.odf.validation;

import java.util.Set;
import java.util.stream.Collectors;

import org.openpreservation.format.xml.Namespace;

class Utils {
    private Utils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String collectNsPrefixes(final Set<Namespace> namespaces) {
        return namespaces.stream().map(Namespace::getPrefix).map(s -> {
            return s.isEmpty() ? "<no-prefix>" : s;
        }).collect(Collectors.joining());
    }
}
