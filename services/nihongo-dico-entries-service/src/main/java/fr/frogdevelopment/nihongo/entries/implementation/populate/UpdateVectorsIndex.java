package fr.frogdevelopment.nihongo.entries.implementation.populate;

import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class UpdateVectorsIndex {

    void call(Connection connection) throws SQLException {
        try (var statement = connection.createStatement()) {

            log.info("- create kanji prgoonga index");
            statement.executeUpdate("CREATE INDEX pgroonga_kanji_index ON entries USING pgroonga (kanji);");

            log.info("- create kana prgoonga index");
            statement.executeUpdate("CREATE INDEX pgroonga_kana_index ON entries USING pgroonga (kana);");

            log.info("- create reading prgoonga index");
            statement.executeUpdate("CREATE INDEX pgroonga_reading_index ON entries USING pgroonga (reading);");

            log.info("- create vocabulary prgoonga index");
            statement.executeUpdate("CREATE INDEX pgroonga_glosses_index ON glosses USING pgroonga (vocabulary);");
        }
    }
}
