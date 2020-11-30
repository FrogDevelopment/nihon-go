package com.frogdevelopment.nihongo.entries.implementation.populate;

import com.frogdevelopment.nihongo.multischema.Language;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
class ClearData {

    void call(final Connection connection) throws SQLException {
        try (final var statement = connection.createStatement()) {

            for (final Language language : Language.values()) {
                log.info("- clearing glosses for {}", language);
                final String schema = language.getCode();
                statement.executeUpdate("TRUNCATE " + schema + ".glosses");
                statement.executeUpdate("DROP INDEX IF EXISTS " + schema + ".pgroonga_glosses_index");
            }

            log.info("- clearing senses");
            statement.executeUpdate("TRUNCATE jpn.senses CASCADE ");

            log.info("- clearing entries");
            statement.executeUpdate("TRUNCATE jpn.entries CASCADE ");
            statement.executeUpdate("DROP INDEX IF EXISTS jpn.pgroonga_kanji_index");
            statement.executeUpdate("DROP INDEX IF EXISTS jpn.pgroonga_kana_index");
            statement.executeUpdate("DROP INDEX IF EXISTS jpn.pgroonga_reading_index");
        }
    }
}
