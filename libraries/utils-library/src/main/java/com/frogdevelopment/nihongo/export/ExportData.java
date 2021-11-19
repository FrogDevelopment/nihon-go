package com.frogdevelopment.nihongo.export;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

import org.postgresql.PGConnection;

import com.frogdevelopment.nihongo.ftp.FtpClient;

@Slf4j
@RequiredArgsConstructor
public final class ExportData {

    private final String remotePath;
    private final DataSource dataSource;
    private final FtpClient ftpClient;
    private final CopyOut copyOut;

    public void call(final Stream<Export> stream) {
        log.info("****** Export data");
        try {
            ftpClient.open();
            try (final var connection = dataSource.getConnection()) {
                final var pgConnection = connection.unwrap(PGConnection.class);
                final var copyManager = pgConnection.getCopyAPI();

                stream
                        .parallel()
                        .map(entry -> copyOut.call(copyManager, entry))
                        .filter(Objects::nonNull)
                        .forEach(this::exportToFtp);
            } finally {
                ftpClient.close();
            }
        } catch (final SQLException | IOException e) {
            throw new IllegalStateException(e);
        }

        log.info("****** Export done");
    }

    private void exportToFtp(final Path path) {
        try {
            log.info("- Uploading {} to {}", path, remotePath);
            final var file = path.toFile();
            ftpClient.putFileToPath(Path.of(remotePath, file.getName()).toString(), file);
        } catch (final IOException e) {
            log.error("Unexpected error while uploading file " + path, e);
        }
    }

    @Value(staticConstructor = "of")
    public static class Export {
        String fileName;
        String copySql;
    }
}
