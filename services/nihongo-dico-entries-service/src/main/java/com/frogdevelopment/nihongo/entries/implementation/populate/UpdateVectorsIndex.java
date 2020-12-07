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
            statement.executeUpdate("CREATE INDEX pgroonga_kanji_index ON jpn.entries USING pgroonga (kanji);");

            log.info("- create kana prgoonga index");
            statement.executeUpdate("CREATE INDEX pgroonga_kana_index ON jpn.entries USING pgroonga (kana);");

            log.info("- create reading prgoonga index");
            statement.executeUpdate("CREATE INDEX pgroonga_reading_index ON jpn.entries USING pgroonga (reading);");

            for (Language language : Language.values()) {
                log.info("- create vocabulary prgoonga index for schema {}", language);
                String schema = language.getCode();
                statement.executeUpdate("CREATE INDEX pgroonga_glosses_index ON " + schema + ".glosses USING pgroonga (vocabulary);");
            }
        }
    }
}
