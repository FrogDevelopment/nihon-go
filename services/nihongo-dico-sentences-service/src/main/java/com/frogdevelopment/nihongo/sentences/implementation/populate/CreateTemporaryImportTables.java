package com.frogdevelopment.nihongo.sentences.implementation.populate;

import java.sql.Connection;
import java.sql.SQLException;

import jakarta.inject.Singleton;

@Singleton
class CreateTemporaryImportTables {

    private static final String SQL_TMP_TABLES = """
             CREATE TEMPORARY TABLE tmp_sentences
             (
                 sentence_id INTEGER NOT NULL,
                 lang        CHAR(4),
                 sentence    TEXT
             ) ON COMMIT PRESERVE ROWS;

            CREATE TEMPORARY TABLE tmp_links
             (
                 sentence_id    INTEGER NOT NULL,
                 translation_id INTEGER NOT NULL
             ) ON COMMIT PRESERVE ROWS;

            CREATE TEMPORARY TABLE tmp_japanese_indices
             (
                 sentence_id INTEGER NOT NULL,
                 meaning_id  INTEGER NOT NULL,
                 linking     TEXT
             ) ON COMMIT PRESERVE ROWS;
             """;

    void call(final Connection connection) throws SQLException {
        try (final var statement = connection.createStatement()) {
            statement.executeUpdate(SQL_TMP_TABLES);
        }
    }

}
