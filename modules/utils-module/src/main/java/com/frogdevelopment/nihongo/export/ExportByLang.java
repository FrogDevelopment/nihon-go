package com.frogdevelopment.nihongo.export;

import static java.nio.charset.StandardCharsets.UTF_8;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.frogdevelopment.nihongo.multischema.Language;

@Slf4j
@RequiredArgsConstructor
class ExportByLang {

    private final PathExportManager pathExportManager;
    private final ExportDao exportDao;

    void call(final Language language) {
        final var code = language.getCode();
        final var location = pathExportManager.getPathForLang(code);
        exportDao.export(code, rs -> {
            try (final var writer = new ExportWriter(location)) {
                writer.start();
                var isFirst = true;
                while (rs.next()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        writer.separate();
                    }
                    writer.add(rs.getString(1));
                }
                writer.end();
            } catch (final IOException e) {
                log.error("Error while writing export file for lang " + code, e);
            }

            return null;
        });
    }

    @Slf4j
    private static final class ExportWriter implements AutoCloseable {

        private final BufferedWriter writer;

        private ExportWriter(final Path path) throws IOException {
            Files.deleteIfExists(path);
            log.info("Exporting into file {}", path);

            this.writer = new BufferedWriter(new FileWriter(path.toFile(), UTF_8));
        }

        private void start() throws IOException {
            this.add("[");
        }

        private void add(String string) throws IOException {
            this.writer.write(string);
        }

        private void separate() throws IOException {
            this.add(",");
        }

        private void end() throws IOException {
            this.add("]");
        }

        @Override
        public void close() throws IOException {
            this.writer.close();
        }
    }
}
