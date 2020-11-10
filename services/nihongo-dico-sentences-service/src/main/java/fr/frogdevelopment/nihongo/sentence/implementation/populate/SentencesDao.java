package fr.frogdevelopment.nihongo.sentence.implementation.populate;

import static java.util.stream.Collectors.joining;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class SentencesDao {

    private static final String LANGS = Stream
            .of("jpn", "dut", "nld", "eng", "fra", "fre", "ger", "deu", "hun", "rus", "spa", "swe", "slv")
            .map(l -> "'" + l + "'")
            .collect(joining(","));

    private final JdbcTemplate jdbcTemplate;

    public SentencesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void prepareImportedData(Connection connection) throws SQLException {
        try (var statement = connection.createStatement()) {
            log.info("- remove sentences without sentence or with un-handle lang");
            statement.executeUpdate("DELETE FROM i_sentences "
                                    + "WHERE sentence IS NULL "
                                    + "OR lang IS NULL "
                                    + "OR lang NOT IN (" + LANGS + ");");

            log.info("- handle lang with multiple iso3 code");
            statement.executeUpdate("UPDATE i_sentences SET lang = "
                                    + "CASE "
                                    + "WHEN lang ='nld' THEN 'dut' "
                                    + "WHEN lang ='fre' THEN 'fra' "
                                    + "WHEN lang ='deu' THEN 'ger' "
                                    + "ELSE lang "
                                    + "END");

            log.info("- remove indices without linking or translation");
            statement.executeUpdate("DELETE FROM i_japanese_indices WHERE linking IS NULL");
            statement.executeUpdate("DELETE FROM i_japanese_indices i "
                                    + "WHERE NOT EXISTS(SELECT 1 FROM i_sentences s WHERE s.sentence_id = i.sentence_id);");
        }
    }

    public void insertSentences(Connection connection) throws SQLException {
        try (var statement = connection.createStatement()) {
            log.info("- cleaning previous data");
            statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_sentences_index");
            statement.executeUpdate("TRUNCATE sentences CASCADE;");

            log.info("- inserting new sentences");
            statement.executeUpdate("INSERT INTO sentences(sentence_id, lang, sentence)"
                                    + " SELECT i.sentence_id, i.lang, i.sentence FROM i_sentences i;");

            log.info("- create sentence_id prgoonga index");
            jdbcTemplate.update("CREATE INDEX pgroonga_sentences_index ON sentences USING pgroonga (sentence);");
        }
    }

    public void insertTranslationsLinks(Connection connection) throws SQLException {
        try (var statement = connection.createStatement()) {
            log.info("- cleaning previous data");
            statement.executeUpdate("TRUNCATE links_japanese_translation CASCADE;");

            log.info("- inserting links for japanese translation");
            statement.executeUpdate("INSERT INTO links_japanese_translation (japanese_id, translation_id) "
                                    + "SELECT japanese.sentence_id, translation.sentence_id "
                                    + "FROM i_links l "
                                    + "INNER JOIN sentences japanese ON japanese.sentence_id = l.sentence_id AND japanese.lang = 'jpn' "
                                    + "INNER JOIN sentences translation ON translation.sentence_id = l.translation_id AND translation.lang != 'jpn';");
        }
    }

    public void insertJapaneseIndices(Connection connection) throws SQLException {
        try (var statement = connection.createStatement()) {
            log.info("- cleaning previous data");
            statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_linking_index");
            statement.executeUpdate("TRUNCATE japanese_indices CASCADE;");

            log.info("- inserting links for japanese entries");
            statement.executeUpdate("INSERT INTO japanese_indices "
                                    + "SELECT i.sentence_id, i.linking FROM i_japanese_indices i "
                                    + "ON CONFLICT DO NOTHING;");

            log.info("- create linking prgoonga index");
            jdbcTemplate.update("CREATE INDEX pgroonga_linking_index ON japanese_indices USING pgroonga (linking);");
        }
    }
}
