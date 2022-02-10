package com.frogdevelopment.nihongo.export;

import com.frogdevelopment.nihongo.ftp.FtpProperties;
import com.frogdevelopment.nihongo.ftp.SftpClientHelper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Singleton
@RequiredArgsConstructor
public final class DataFtpExporter {

    private final FtpProperties ftpProperties;
    private final DataSource dataSource;
    private final SftpClientHelper ftpClientHelper;

    public void export(final Collection<Export> collection) {
        export(collection.stream());
    }

    public void export(final Stream<Export> stream) {
        log.info("****** Export data into {}", ftpProperties.getServer());
        try {
            ftpClientHelper.open();
            try (final var connection = dataSource.getConnection()) {
                final var pgConnection = connection.unwrap(PGConnection.class);
                final var copyManager = pgConnection.getCopyAPI();

                stream
                        .map(export -> exportToArchive(copyManager, export))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(this::exportToFtp);
            } finally {
                ftpClientHelper.close();
            }
        } catch (final SQLException | IOException e) {
            throw new IllegalStateException(e);
        }

        log.info("****** Export done");
    }

    private Optional<Path> exportToArchive(final CopyManager copyManager, final Export export) {
        String fileName = export.fileName();
        try {
            final var tempDirectory = Files.createTempDirectory("export");
            final var tarPath = Path.of(tempDirectory.toString(), "%s.tar".formatted(fileName));
            log.info("- Creating archive {}", tarPath);
            try (final var outputStream = Files.newOutputStream(tarPath);
                 final var gzo = new GzipCompressorOutputStream(outputStream);
                 final var archiveOutputStream = new TarArchiveOutputStream(gzo)) {
                final var path = Path.of(tempDirectory.toString(), "%s.csv".formatted(fileName));
                final var file = path.toFile();
                try (final var fileWriter = new BufferedWriter(new FileWriter(file, UTF_8))) {
                    copyManager.copyOut(export.copySql(), fileWriter);
                }

                archiveOutputStream.putArchiveEntry(archiveOutputStream.createArchiveEntry(file, file.getName()));
                try (final var inputStream = Files.newInputStream(path)) {
                    IOUtils.copy(inputStream, archiveOutputStream);
                }
                archiveOutputStream.closeArchiveEntry();
                archiveOutputStream.finish();
            }

            return Optional.of(tarPath);

        } catch (final IOException | SQLException e) {
            log.error("Unexpected error while creating archive for " + fileName, e);
            return Optional.empty();
        }
    }

    private void exportToFtp(final Path path) {
        try {
            log.info("- Uploading {} to {}", path, ftpProperties.getRemotePath());
            final var file = path.toFile();
            ftpClientHelper.storeFile(Path.of(ftpProperties.getRemotePath(), file.getName()).toString(), file);
        } catch (final IOException e) {
            log.error("Unexpected error while uploading file " + path, e);
        }
    }

    public record Export(String fileName, String copySql) {    }
}
