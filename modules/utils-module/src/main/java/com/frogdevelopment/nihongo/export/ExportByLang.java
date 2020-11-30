package com.frogdevelopment.nihongo.export;

import com.frogdevelopment.nihongo.multischema.Language;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RequiredArgsConstructor
final class ExportByLang {

    private final PathExportManager pathExportManager;
    private final ExportDao exportDao;

    void call(final Language language) {
        final var code = language.getCode();
        final var location = pathExportManager.getPathForLang(code);
        exportDao.export(code, rs -> {
            try (final var writer = new ExportWriter(location)) {
                while (rs.next()) {
                    writer.write(rs);
                }
            } catch (final IOException e) {
                log.error("Error while writing export file for lang " + code, e);
            }

            return null;
        });
    }

    @Slf4j
    static final class ExportWriter implements AutoCloseable {

        private final String fileName;
        private final BufferedWriter writer;

        ExportWriter(final Path path) throws IOException {
            this.fileName = path.getFileName().toString();

            Files.deleteIfExists(path);
            log.info("Exporting into file {}", fileName);

            this.writer = new BufferedWriter(new FileWriter(path.toFile(), UTF_8));
            this.writer.write("[");
        }

        void write(final ResultSet rs) throws SQLException, IOException {
            this.writer.write(rs.getString(1));
            this.writer.write(",");
        }

        @Override
        public void close() throws IOException {
            this.writer.write("]");
            log.debug("Closing file {}", fileName);
            this.writer.close();
        }
    }
}
