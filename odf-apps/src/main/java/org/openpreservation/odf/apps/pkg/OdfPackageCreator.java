package org.openpreservation.odf.apps.pkg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Callable;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "odf-zip", mixinStandardHelpOptions = true, version = "odf-zip 0.1", description = "Creates an ODF compliant zip from a directory.")
class OdfPackageCreator implements Callable<Integer> {
    private static final String MIMETYPE = "mimetype";
    private static final String ODS_MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";
    @Option(names = { "-c",
            "--compress-mime" }, defaultValue = "false", description = "Compress mimetype entry with defalte algorithm.")
    private boolean compressMimetype;
    @Parameters(paramLabel = "FOLDERS", arity = "1..*", description = "A list of Open Document Format spreadsheet file to validate.")
    private File[] toZipFolders;

    @Override
    public Integer call() throws IOException {
        for (File folder : this.toZipFolders) {
            zipFolder(folder.toPath(), this.compressMimetype);
        }
        return 0;
    }

    /**
     * Stolen shamelessly from
     * https://mkyong.com/java/how-to-compress-files-in-zip-format/
     * 
     * @param source
     * @throws IOException
     */
    public static void zipFolder(Path source, final boolean compressMimetype) throws IOException {

        // get folder name as zip file name
        String zipFileName = source.getFileName().toString() + ".ods";

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(
                new FileOutputStream(zipFileName))) {
            handleMimetype(source, zos, compressMimetype);
            Files.walkFileTree(source, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file,
                        BasicFileAttributes attributes) throws IOException {

                    final String name = source.relativize(file).toString();
                    // only copy files, no symbolic links
                    if (attributes.isSymbolicLink() || name.equals(MIMETYPE)) {
                        return FileVisitResult.CONTINUE;
                    }

                    try (FileInputStream fis = new FileInputStream(file.toFile())) {
                        zos.putArchiveEntry(createDeflatedEntry(name, attributes));
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }

                        zos.closeArchiveEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.err.printf("Unable to zip : %s%n%s%n", file, exc);
                    throw exc;
                }
            });

        }
    }

    private static void handleMimetype(Path source, ZipArchiveOutputStream zos, final boolean compressMimetype)
            throws IOException {
        final Path mimetype = source.resolve(MIMETYPE);
        if (!Files.exists(mimetype)) {
            byte[] mimetypeBytes = ODS_MIMETYPE.getBytes(StandardCharsets.US_ASCII);
            ZipArchiveEntry mimetypeEntry = (compressMimetype) ? createDeflatedEntry(MIMETYPE, mimetypeBytes.length)
                    : createStoredEntry(MIMETYPE, mimetypeBytes.length, crc(mimetypeBytes));
            zos.putArchiveEntry(mimetypeEntry);
            zos.write(mimetypeBytes);
        } else {
            try (FileInputStream fis = new FileInputStream(mimetype.toFile())) {
                ZipArchiveEntry mimetypeEntry = (compressMimetype)
                        ? createDeflatedEntry(MIMETYPE, mimetype.toFile().length())
                        : createStoredEntry(MIMETYPE, Files.readAttributes(mimetype, BasicFileAttributes.class),
                                crc(mimetype));
                zos.putArchiveEntry(mimetypeEntry);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
            }
        }
        zos.closeArchiveEntry();
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new OdfPackageCreator()).execute(args);
        System.exit(exitCode);
    }

    private static ZipArchiveEntry createStoredEntry(final String name, final long size, final long crc) {
        ZipArchiveEntry zipEntry = createZipEntry(name, size);
        zipEntry.setMethod(ZipEntry.STORED);
        zipEntry.setCompressedSize(size);
        zipEntry.setCrc(crc);
        return zipEntry;
    }

    private static ZipArchiveEntry createStoredEntry(final String name, BasicFileAttributes attrs, final long crc) {
        ZipArchiveEntry zipEntry = createStoredEntry(name, attrs.size(), crc);
        return addEntryDetails(zipEntry, attrs);
    }

    private static ZipArchiveEntry createDeflatedEntry(final String name, final long size) {
        ZipArchiveEntry zipEntry = createZipEntry(name, size);
        zipEntry.setMethod(ZipEntry.DEFLATED);
        return zipEntry;
    }

    private static ZipArchiveEntry createDeflatedEntry(final String name, final BasicFileAttributes attrs) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        return addEntryDetails(zipEntry, attrs);
    }

    private static ZipArchiveEntry createZipEntry(final String name, final long size) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        zipEntry.setSize(size);
        return zipEntry;
    }

    private static ZipArchiveEntry addEntryDetails(final ZipArchiveEntry zipEntry, final BasicFileAttributes attrs) {
        zipEntry.setCreationTime(attrs.creationTime());
        zipEntry.setLastAccessTime(attrs.lastAccessTime());
        zipEntry.setLastModifiedTime(attrs.lastModifiedTime());
        zipEntry.setSize(attrs.size());
        return zipEntry;
    }

    private static long crc(final Path toCrc) throws IOException {
        try (FileInputStream fis = new FileInputStream(toCrc.toFile())) {
            CRC32 crc = new CRC32();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                crc.update(buffer, 0, len);
            }
            return crc.getValue();
        }
    }

    private static long crc(final byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes, 0, bytes.length);
        return crc.getValue();
    }
}
