package com.frogdevelopment.nihongo.sentences.implementation.populate;

import com.frogdevelopment.nihongo.multischema.Language;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Slf4j
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class SentencesDao {

    private final JdbcTemplate jdbcTemplate;

    public SentencesDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void prepareImportedData(final Connection connection) throws SQLException {
        try (final var statement = connection.createStatement()) {
//             log.info("- remove sentences without translation");
//             statement.executeUpdate("""
//                     DELETE FROM tmp_sentences s
//                     WHERE NOT EXISTS(SELECT 1 FROM tmp_links links
//                     WHERE (links.sentence_id = s.sentence_id AND s.lang = 'jpn')
//                      OR links.translation_id = s.sentence_id AND s.lang != 'jpn');
//                     """);

            log.info("- remove empty sentences or with un-handle lang");
            final var langs = Arrays.stream(Language.values())
                    .map(Language::getCode)
                    .collect(Collectors.toSet());
            langs.addAll(Set.of("jpn", "nld", "fre", "deu"));
            final var languagesToKeep = langs.stream()
                    .map(l -> "'" + l + "'")
                    .collect(joining(","));

            statement.executeUpdate("DELETE FROM tmp_sentences "
                    + "WHERE sentence IS NULL "
                    + "OR lang IS NULL "
                    + "OR lang NOT IN (" + languagesToKeep + ");");

            log.info("- handle lang with multiple iso3 code");
            statement.executeUpdate("""
                    UPDATE tmp_sentences SET lang =
                    CASE
                        WHEN lang ='nld' THEN 'dut'
                        WHEN lang ='fre' THEN 'fra'
                        WHEN lang ='deu' THEN 'ger'
                        ELSE lang
                    END
                    """);

            log.info("- remove indices without linking or translation");
            statement.executeUpdate("DELETE FROM tmp_japanese_indices WHERE linking IS NULL");
            statement.executeUpdate("""
                    DELETE FROM tmp_japanese_indices i
                    WHERE NOT EXISTS(SELECT 1 FROM tmp_sentences s WHERE s.sentence_id = i.sentence_id);
                    """);
        }
    }

    public Map<String, Integer> insertSentences(final Connection connection) throws SQLException {
        final var data = new HashMap<String, Integer>();
        final var langs = Arrays.stream(Language.values())
                .map(Language::getCode)
                .collect(Collectors.toSet());
        langs.addAll(Set.of("jpn"));

        for (final String lang : langs) {
            log.info("Insert sentences for [{}]", lang);
            connection.setSchema(lang);
            try (final var statement = connection.createStatement()) {
                log.info("- cleaning previous data");
                statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_sentences_index");
                statement.executeUpdate("TRUNCATE sentences CASCADE;");

                log.info("- inserting new sentences");
                final var nbInserted = statement.executeUpdate("INSERT INTO sentences(sentence_id, sentence)"
                        + " SELECT tmp.sentence_id, tmp.sentence FROM tmp_sentences tmp"
                        + " WHERE tmp.lang = '" + lang + "';");
                data.put(lang, nbInserted);
                log.info("-> {} sentences inserted", nbInserted);

                log.info("- create sentence_id prgoonga index");
                jdbcTemplate.update("CREATE INDEX pgroonga_sentences_index ON " + lang + ".sentences USING pgroonga (sentence);");
            }
        }

        return data;
    }

    public void insertTranslationsLinks(final Connection connection) throws SQLException {
        for (final Language language : Language.values()) {
            final String lang = language.getCode();
            log.info("Insert Translation links for [{}]", lang);
            connection.setSchema(lang);
            try (final var statement = connection.createStatement()) {
                log.info("- cleaning previous data");
                statement.executeUpdate("TRUNCATE links_japanese_translation CASCADE;");

                log.info("- inserting links for japanese translation");
                final var sql = """
                        INSERT INTO links_japanese_translation (japanese_id, translation_id)
                        SELECT japanese.sentence_id, translation.sentence_id
                        FROM tmp_links tmp
                        INNER JOIN jpn.sentences japanese ON japanese.sentence_id = tmp.sentence_id
                        INNER JOIN sentences translation ON translation.sentence_id = tmp.translation_id;
                        """;
                statement.executeUpdate(sql);
            }
        }
    }

    public void insertJapaneseIndices(final Connection connection) throws SQLException {
        log.info("Insert Japaneses Indices");
        connection.setSchema("jpn");
        try (final var statement = connection.createStatement()) {
            log.info("- cleaning previous data");
            statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_linking_index");
            statement.executeUpdate("TRUNCATE japanese_indices CASCADE;");

            log.info("- inserting links for japanese entries");
            statement.executeUpdate("""
                    INSERT INTO japanese_indices
                    SELECT tmp.sentence_id, tmp.linking FROM tmp_japanese_indices tmp
                    ON CONFLICT DO NOTHING;
                    """);

            log.info("- create linking prgoonga index");
            jdbcTemplate.update("CREATE INDEX pgroonga_linking_index ON jpn.japanese_indices USING pgroonga (linking);");
        }
    }
}
