package com.frogdevelopment.nihongo.sentences.implementation.populate;

import com.frogdevelopment.nihongo.Language;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static javax.transaction.Transactional.TxType.MANDATORY;

@Slf4j
@JdbcRepository
@Transactional(MANDATORY)
public class SentencesDao {

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

            statement.executeUpdate("""
                    DELETE FROM tmp_sentences
                    WHERE sentence IS NULL
                        OR lang IS NULL
                        OR lang NOT IN (%s);""".formatted(languagesToKeep));

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
        try (final var statement = connection.createStatement()) {
            statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_sentences_index");
            statement.executeUpdate("TRUNCATE sentences CASCADE;");

            try (final var preparedStatement = connection.prepareStatement("""
                    INSERT INTO sentences(sentence_id, sentence, language)
                        SELECT tmp.sentence_id, tmp.sentence, tmp.lang
                        FROM tmp_sentences tmp
                        WHERE tmp.lang = ?;""")) {
                data.put("jpn", insertSentences(preparedStatement, "jpn"));
                for (Language language : Language.values()) {
                    final var languageCode = language.getCode();
                    data.put(languageCode, insertSentences(preparedStatement, languageCode));
                }
            }

            log.info("- create sentence_id prgoonga index");
            statement.executeUpdate("CREATE INDEX pgroonga_sentences_index ON sentences USING pgroonga (sentence);");
        }

        return data;
    }

    private int insertSentences(PreparedStatement preparedStatement, String language) throws SQLException {
        log.info("Insert sentences for [{}]", language);
        preparedStatement.setString(1, language);
        final var nbInserted = preparedStatement.executeUpdate();
        log.info("-> {} sentences inserted", nbInserted);

        return nbInserted;
    }

    public void insertTranslationsLinks(final Connection connection) throws SQLException {
        log.info("Insert Translation links");
        try (final var statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE links_japanese_translation CASCADE;");
            statement.executeUpdate("""
                    INSERT INTO links_japanese_translation (japanese_id, translation_id)
                    SELECT japanese.sentence_id, translation.sentence_id
                    FROM tmp_links tmp
                    INNER JOIN sentences japanese ON japanese.sentence_id = tmp.sentence_id
                    INNER JOIN sentences translation ON translation.sentence_id = tmp.translation_id;
                    """);
        }
    }

    public void insertJapaneseIndices(final Connection connection) throws SQLException {
        log.info("Insert Japaneses Indices");
        try (final var statement = connection.createStatement()) {
            statement.executeUpdate("DROP INDEX IF EXISTS pgroonga_linking_index");
            statement.executeUpdate("TRUNCATE japanese_indices CASCADE;");
            statement.executeUpdate("""
                    INSERT INTO japanese_indices
                    SELECT tmp.sentence_id, tmp.linking FROM tmp_japanese_indices tmp
                    ON CONFLICT DO NOTHING;
                    """);

            log.info("- create linking prgoonga index");
            statement.executeUpdate("CREATE INDEX pgroonga_linking_index ON japanese_indices USING pgroonga (linking);");
        }
    }
}
