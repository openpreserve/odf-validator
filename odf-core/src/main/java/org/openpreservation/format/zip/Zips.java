package org.openpreservation.format.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.openpreservation.utils.Checks;

public class Zips {
    private Zips() {
        throw new AssertionError("Utility class 'Zips' should not be instantiated");
    }

    public static final ZipEntryProcessor getExtractorInstance(final Path root, boolean extractDirectories) {
        Checks.notNull(root, "Path", "root");
        return new ZipEntryProcessor() {
            @Override
            public void process(final ZipEntry entry, final InputStream is) throws IOException {
                if (!this.accepts(entry)) {
                    return;
                }
                final Path target = root.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(target);
                } else {
                    Files.createDirectories(target.getParent());
                    try (OutputStream os = Files.newOutputStream(target)) {
                        Checks.copyStream(is, os);
                    }
                }
            }

            @Override
            public boolean accepts(ZipEntry entry) {
                return !entry.isDirectory() || extractDirectories;
            }

        };
    }

    public static final ZipProcessor.Factory getFactoryInstance() {
        return new ZipProcessor.Factory() {
            @Override
            public ZipProcessor from(ZipEntryProcessor processor) {
                return new ZipProcessor() {
                    @Override
                    public ZipArchive process(final InputStream is) throws IOException {
                        List<ZipEntry> entries = new ArrayList<>();
                        try (ZipInputStream zis = new ZipInputStream(is)) {
                            ZipEntry entry;
                            while ((entry = zis.getNextEntry()) != null) {
                                entries.add(entry);
                                processor.process(entry, zis);
                            }
                        }
                        return ZipArchiveImpl.from(entries);
                    }
                };
            };
        };
    }
}
