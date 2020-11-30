package com.frogdevelopment.nihongo.sentences.implementation.export;

import com.frogdevelopment.nihongo.export.ExportDao;
import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ExportDaoImpl implements ExportDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void export(final String lang, final ResultSetExtractor<Void> rse) {
        @Language(value = "SQL") final var sql = """
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

        jdbcTemplate.query(String.format(sql, lang), rse);
    }
}
