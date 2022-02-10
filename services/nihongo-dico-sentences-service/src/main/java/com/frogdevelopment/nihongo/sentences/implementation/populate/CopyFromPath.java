package com.frogdevelopment.nihongo.sentences.implementation.populate;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.postgresql.jdbc.PgConnection;

import jakarta.inject.Singleton;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newInputStream;

/**
 * https://tatoeba.org/eng/downloads
 */
@Slf4j
@Singleton
class CopyFromPath {

    private static final String SQL_COPY = "COPY %s (%s) FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false, NULL '')";

    void call(final Connection connection, final Path path, final String tableName, final String columns)
            throws IOException, SQLException {
        // cf http://stackoverflow.com/a/28029231/244911
        try ( // 1st InputStream from your compressed file
              final var in = new BufferedInputStream(newInputStream(path));
              // wrap in a 2nd InputStream that deals with compression
              final var bzIn = new BZip2CompressorInputStream(in);
              // wrap in a 3rd InputStream that deals with tar
              final var tarIn = new TarArchiveInputStream(bzIn)) {

            ArchiveEntry entry;
            while (null != (entry = tarIn.getNextEntry())) {
                if (entry.getSize() < 1) {
                    continue;
                }

                // cf http://stackoverflow.com/a/25749756/244911
                // Read directly from tarInput
                final var sqlCopy = String.format(SQL_COPY, tableName, columns);
                final var nbInserted = connection
                        .unwrap(PgConnection.class)
                        .getCopyAPI()
                        .copyIn(sqlCopy, new InputStreamReader(tarIn, UTF_8));
                log.info("\t{} lines inserted", nbInserted);
            }
        }
    }
}
