package com.frogdevelopment.nihongo.entries.implementation.populate;

import com.frogdevelopment.nihongo.entries.implementation.Language;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
class ClearData {

    void call(Connection connection) throws SQLException {
        try (var statement = connection.createStatement()) {

            for (Language language : Language.values()) {
                String schema = language.getCode();
                log.info("- clearing glosses for {}", schema);
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
