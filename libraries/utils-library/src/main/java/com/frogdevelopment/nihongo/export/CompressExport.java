package com.frogdevelopment.nihongo.export;

import static com.frogdevelopment.nihongo.export.ExportData.Export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;

@Slf4j
@RequiredArgsConstructor
class CompressExport {

    @FunctionalInterface
    public interface ExportFunction {
        void accept(final Path path) throws IOException, SQLException;
    }

    Path call(final Export export, final ExportFunction exportFunction) {
        final var fileName = export.getFileName();
        try {
            final Path tempDirectory = Files.createTempDirectory("export");
            final Path tarPath = Path.of(tempDirectory.toString(), "%s.tar".formatted(fileName));
            log.info("- Creating archive {}", tarPath);
            try (final OutputStream fo = Files.newOutputStream(tarPath);
                 final OutputStream gzo = new GzipCompressorOutputStream(fo);
                 final ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(gzo)) {
                final Path path = Path.of(tempDirectory.toString(), "%s.csv".formatted(fileName));
                exportFunction.accept(path);
                final var file = path.toFile();
                archiveOutputStream.putArchiveEntry(archiveOutputStream.createArchiveEntry(file, file.getName()));
                try (final InputStream inputStream = Files.newInputStream(path)) {
                    IOUtils.copy(inputStream, archiveOutputStream);
                }
                archiveOutputStream.closeArchiveEntry();
                archiveOutputStream.finish();
            }

            return tarPath;

        } catch (final IOException | SQLException e) {
            log.error("Unexpected error while creating archive for " + fileName, e);
            return null;
        }
    }

}
