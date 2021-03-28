package com.frogdevelopment.nihongo.export;

import com.frogdevelopment.nihongo.FtpClient;
import com.frogdevelopment.nihongo.multischema.Language;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public final class ExportData {

    private final String remotePath;
    private final DataSource dataSource;
    private final FtpClient ftpClient;
    private final CopyOut copyOut;

    public void call(final Function<String, String> copySqlSupplier) {
        log.info("****** Export data");
        try {
            ftpClient.open();
            try (final var connection = dataSource.getConnection()) {
                final var pgConnection = connection.unwrap(PGConnection.class);
                final var copyManager = pgConnection.getCopyAPI();

                Arrays.stream(Language.values())
                        .parallel()
                        .map(Language::getCode)
                        .map(lang -> copyOut.call(copyManager, copySqlSupplier.apply(lang), lang))
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
            ftpClient.putFileToPath(Path.of(remotePath, path.toFile().getName()).toString(), path.toFile());
        } catch (final IOException e) {
            log.error("Unexpected error while uploading file " + path, e);
        }
    }
}
