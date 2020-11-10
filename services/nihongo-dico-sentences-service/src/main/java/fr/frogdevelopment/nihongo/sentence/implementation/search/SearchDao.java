package fr.frogdevelopment.nihongo.sentence.implementation.search;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import fr.frogdevelopment.nihongo.sentence.implementation.search.entity.Sentence;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
                  + "j.sentence AS japanese,"
                  + "t.sentence AS translation"
                  + " FROM sentences j"
                  + " INNER JOIN links_japanese_translation l ON l.japanese_id = j.sentence_id"
                  + " INNER JOIN sentences t ON t.sentence_id = l.translation_id AND t.lang = ? AND t.sentence &@~ ?"
                  + " INNER JOIN japanese_indices i ON i.japanese_id = j.sentence_id AND i.linking &@~ ?";

        String[] params = {lang, queryGloss, queryJapanese};

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
