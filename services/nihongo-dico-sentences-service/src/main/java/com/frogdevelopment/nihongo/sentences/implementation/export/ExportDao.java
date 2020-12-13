package com.frogdevelopment.nihongo.sentences.implementation.export;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExportDao {

    private final JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> call(String lang) {
        var sql = """
                SELECT JSON_BUILD_OBJECT(
                      'linking', i.linking,
                      'japanese', japanese.sentence,
                      'translations', ARRAY_AGG(translation.sentence))
                FROM jpn.sentences japanese
                        INNER JOIN %1$s.links_japanese_translation links ON links.japanese_id = japanese.sentence_id
                        INNER JOIN %1$s.sentences translation ON translation.sentence_id = links.translation_id
                        INNER JOIN jpn.japanese_indices i ON i.japanese_id = japanese.sentence_id
                GROUP BY i.linking, japanese.sentence
                """;

        return jdbcTemplate.queryForList(String.format(sql, lang), String.class);
    }
}
