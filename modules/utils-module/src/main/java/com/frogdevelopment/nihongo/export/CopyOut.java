package com.frogdevelopment.nihongo.export;

import static java.nio.charset.StandardCharsets.UTF_8;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Function;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;

import com.frogdevelopment.nihongo.multischema.Language;

@Slf4j
@RequiredArgsConstructor
class CopyOut {

    private final DataSource dataSource;
    private final PathExportManager pathExportManager;

    void call(final Function<String, String> copySqlSupplier) {
        try (final var connection = dataSource.getConnection()) {
            final var pgConnection = connection.unwrap(PGConnection.class);
            final var copyManager = pgConnection.getCopyAPI();

            Arrays.stream(Language.values())
//                        .parallel() // fixme to test the impact on performance
                    .map(Language::getCode)
                    .forEach(lang -> copyOut(copyManager, copySqlSupplier.apply(lang), pathExportManager.getPathForLang(lang)));
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void copyOut(final CopyManager copyManager, final String sql, final Path path) {
        log.info("****** Export to {}", path);
        try (final var out = new BufferedWriter(new FileWriter(path.toFile(), UTF_8))) {
            copyManager.copyOut(sql, out);
        } catch (IOException | SQLException e) {
            log.error("Unexpected error while exporting to " + path, e);
        }
    }
}
