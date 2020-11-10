package fr.frogdevelopment.nihongo.entries.implementation.export;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ResultSetExtractor;

@Slf4j
class ExportWriter implements Closeable {

    static final String EXPORT_FORMAT = "/export/%s.json";

    private final String fileName;
    private final BufferedWriter writer;

    static ResultSetExtractor<Boolean> exportToFile(String lang) {
        return rs -> {
            try (var writer = new ExportWriter(lang)) {
                while (rs.next()) {
                    writer.write(rs);
                }
            } catch (IOException e) {
                log.error("Error while writing file export for " + lang, e);
                return false;
            }

            return true;
        };
    }

    private ExportWriter(String lang) throws IOException {
        fileName = String.format(EXPORT_FORMAT, lang);
        var path = Paths.get(fileName);
        Files.deleteIfExists(path);
        writer = new BufferedWriter(new FileWriter(path.toFile(), UTF_8));
        log.info("Starting file {}", fileName);
    }

    private void write(ResultSet rs) throws SQLException, IOException {
        writer.write(rs.getString(1));
        writer.newLine();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing file {}", fileName);
        writer.close();
    }
}
