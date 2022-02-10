package com.frogdevelopment.nihongo.sentences.implementation.search;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import com.frogdevelopment.nihongo.sentences.implementation.search.entity.Sentence;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

// fixme to improve: query with kana can be false positive (same kana, but another kanji)
@JdbcRepository
@RequiredArgsConstructor
class SearchDao {

    private final JdbcOperations jdbcOperations;

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Sentence> getSentences(final String lang, final String kanji, final String kana, final String gloss) {
        final var queryGloss = getGloss(gloss);
        final var queryJapanese = getQuery(kanji, kana);

        final var sql = """
                SELECT i.linking,
                    japanese.sentence AS japanese,
                    translation.sentence AS translation
                FROM japanese_sentences japanese
                    INNER JOIN links_japanese_translation link ON link.japanese_id = japanese.sentence_id
                    INNER JOIN translated_sentences translation ON translation.sentence_id = link.translation_id AND translation.sentence &@~ ?
                    INNER JOIN japanese_indices i ON i.japanese_id = japanese.sentence_id AND i.linking &@~ ?""";

        return jdbcOperations.prepareStatement(sql, statement -> {
            statement.setString(1, queryGloss);
            statement.setString(2, queryJapanese);
            final var resultSet = statement.executeQuery();

            final List<Sentence> sentences = new ArrayList<>();
            final var sentenceBuilder = Sentence.builder();
            while (resultSet.next()) {
                sentences.add(sentenceBuilder
                        .japanese(resultSet.getString("japanese"))
                        .translation(resultSet.getString("translation"))
                        .build());
            }

            return sentences;
        });
    }

    private static String getGloss(String gloss) {
        gloss = gloss.replace("'", "''");
        return of(gloss.split(","))
                .map(String::trim)
                .collect(joining(" OR "));
    }

    private static String getQuery(final String kanji, final String kana) {
        return isNotBlank(kanji) ? format("%s OR %s", kanji, kana) : kana;
    }
}
