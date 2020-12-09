package com.frogdevelopment.nihongo.entries.implementation.export;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
class ExportWriter implements AutoCloseable {

    static final String EXPORT_PATH = "/opt/export";
    static final String EXPORT_FORMAT = "%s.json";

    private final String fileName;
    private final BufferedWriter writer;

    static void initExportDirectories() throws IOException {
        var path = Paths.get(EXPORT_PATH);
        if (path.toFile().exists()) {
            FileUtils.cleanDirectory(path.toFile());
        } else {
            Files.createDirectories(path);
        }
    }

    ExportWriter(String lang) throws IOException {
        fileName = String.format(EXPORT_FORMAT, lang);
        var path = Paths.get(EXPORT_PATH, fileName);
        Files.deleteIfExists(path);
        writer = new BufferedWriter(new FileWriter(path.toFile(), UTF_8));
        log.info("Starting file {}", fileName);
    }

    void write(ResultSet rs) throws SQLException, IOException {
        writer.write(rs.getString(1));
        writer.newLine();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing file {}", fileName);
        writer.close();
    }
}
