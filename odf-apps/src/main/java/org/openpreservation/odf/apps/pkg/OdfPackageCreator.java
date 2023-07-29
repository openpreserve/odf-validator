package org.openpreservation.odf.apps.pkg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
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
import org.openpreservation.odf.pkg.OdfPackages;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "odf-zip", mixinStandardHelpOptions = true, version = "odf-zip 0.1", description = "Creates an ODF compliant zip from a directory.")
class OdfPackageCreator implements Callable<Integer> {
    @Option(names = { "-c",
            "--compress-mime" }, defaultValue = "false", description = "Compress mimetype entry with defalte algorithm.")
    private boolean compressMimetype;
    @Option(names = { "-m",
            "--mime-last" }, defaultValue = "false", description = "Add the mimetype file at the end of the file, rather than the start.")
    private boolean mimeLast;
    @Parameters(paramLabel = "FOLDERS", arity = "1..*", description = "A list of Open Document Format spreadsheet file to validate.")
    private File[] toZipFolders;

    @Override
    public Integer call() throws IOException {
        // Loop over the supplied folders and pass them to the creator
        for (File folder : this.toZipFolders) {
            zipFolder(folder.toPath(), this.compressMimetype, this.mimeLast);
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
    public static void zipFolder(Path source, final boolean compressMimetype, final boolean mimeLast)
            throws IOException {
        // The archive is the same name as the supplied directory with a .ods extension
        String zipFileName = source.getFileName().toString() + ".ods";

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(
                new FileOutputStream(zipFileName))) {
            // Special handling of the mimetype file
            if (!mimeLast) {
                handleMimetype(source, zos, compressMimetype);
            }
            Files.walkFileTree(source, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    // For empty directories, we need to add an entry and continue.
                    try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
                        if (!dirStream.iterator().hasNext()) {
                            zos.putArchiveEntry(
                                    addEntryDetails(createZipEntry(source.relativize(dir).toString() + "/", 0), attrs));
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file,
                        BasicFileAttributes attributes) throws IOException {
                    final String name = source.relativize(file).toString();
                    // Ignore the mimetype file, it has already been handled,
                    // only copy files, no symbolic links
                    if (attributes.isSymbolicLink() || name.equals(OdfPackages.MIMETYPE)) {
                        return FileVisitResult.CONTINUE;
                    }

                    // Create a deflated zip entry and add it to the archive
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
            // Add mimetype file at the end if we want a brokend package
            if (mimeLast) {
                handleMimetype(source, zos, compressMimetype);
            }
        }
    }

    private static void handleMimetype(Path source, ZipArchiveOutputStream zos, final boolean compressMimetype)
            throws IOException {
        final Path mimetype = source.resolve(OdfPackages.MIMETYPE);
        if (!Files.exists(mimetype)) {
            return;
        }
        try (FileInputStream fis = new FileInputStream(mimetype.toFile())) {
            ZipArchiveEntry mimetypeEntry = (compressMimetype)
                    ? createDeflatedEntry(OdfPackages.MIMETYPE, mimetype.toFile().length())
                    : createStoredEntry(OdfPackages.MIMETYPE, Files.readAttributes(mimetype, BasicFileAttributes.class),
                            crc(mimetype));
            zos.putArchiveEntry(mimetypeEntry);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
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
