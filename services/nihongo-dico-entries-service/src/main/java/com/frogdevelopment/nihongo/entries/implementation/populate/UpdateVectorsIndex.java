package com.frogdevelopment.nihongo.entries.implementation.populate;

import com.frogdevelopment.nihongo.entries.implementation.Language;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
class UpdateVectorsIndex {

    void call(Connection connection) throws SQLException {
        try (var statement = connection.createStatement()) {

            log.info("- create kanji prgoonga index");
            statement.executeUpdate("CREATE INDEX jpn.pgroonga_kanji_index ON entries USING pgroonga (kanji);");

            log.info("- create kana prgoonga index");
            statement.executeUpdate("CREATE INDEX jpn.pgroonga_kana_index ON entries USING pgroonga (kana);");

            log.info("- create reading prgoonga index");
            statement.executeUpdate("CREATE INDEX jpn.pgroonga_reading_index ON entries USING pgroonga (reading);");

            for (Language language : Language.values()) {
                String schema = language.getCode();
                log.info("- create vocabulary prgoonga index for schema {}", schema);
                statement.executeUpdate("CREATE INDEX pgroonga_glosses_index ON " + schema + ".glosses USING pgroonga (vocabulary);");
            }
        }
    }
}
