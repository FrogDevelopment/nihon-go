package com.frogdevelopment.nihongo.sentences.implementation.search;

import com.frogdevelopment.nihongo.sentences.implementation.search.entity.Sentence;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

// fixme to improve: query with kana can be false positive (same kana, but another kanji)
@Repository
class SearchDao {

    private final JdbcTemplate jdbcTemplate;

    SearchDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(propagation = REQUIRED)
    public List<Sentence> getSentences(String lang, String kanji, String kana, String gloss) {
        var queryGloss = getGloss(gloss);
        var queryJapanese = getQuery(kanji, kana);

        var sql = "SELECT i.linking,"
                + "japanese.sentence AS japanese,"
                + "translation.sentence AS translation"
                + " FROM jpn.sentences japanese"
                + " INNER JOIN " + lang + ".links_japanese_translation link ON link.japanese_id = japanese.sentence_id"
                + " INNER JOIN " + lang + ".sentences translation ON translation.sentence_id = link.translation_id AND translation.sentence &@~ ?"
                + " INNER JOIN jpn.japanese_indices i ON i.japanese_id = japanese.sentence_id AND i.linking &@~ ?";

        String[] params = {queryGloss, queryJapanese};

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> Sentence.builder()
                .japanese(rs.getString("japanese"))
                .translation(rs.getString("translation"))
                .build());
    }

    private static String getGloss(String gloss) {
        gloss = gloss.replaceAll("'", "''");
        return of(gloss.split(","))
                .map(String::trim)
                .collect(joining(" OR "));
    }

    private static String getQuery(String kanji, String kana) {
        return isNotBlank(kanji) ? format("%s OR %s", kanji, kana) : kana;
    }
}
