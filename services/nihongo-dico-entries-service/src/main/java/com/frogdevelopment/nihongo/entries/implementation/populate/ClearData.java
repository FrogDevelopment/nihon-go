package com.frogdevelopment.nihongo.entries.implementation.populate;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

import jakarta.inject.Singleton;

@Slf4j
@Singleton
class ClearData {

    void call(final Connection connection) throws SQLException {
        try (final var statement = connection.createStatement()) {
            log.info("- clearing glosses");
            statement.executeUpdate("TRUNCATE glosses");
            statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_glosses_index");

            log.info("- clearing senses");
            statement.executeUpdate("TRUNCATE senses CASCADE ");

            log.info("- clearing entries");
            statement.executeUpdate("TRUNCATE entries CASCADE ");
            statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_kanji_index");
            statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_kana_index");
            statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_reading_index");
        }
    }
}
