package org.openpreservation.format.xml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface XmlParser {
   public ParseResult parse(final InputStream toTest) throws IOException;
   public ParseResult parse(final Path toTest) throws IOException;
}
