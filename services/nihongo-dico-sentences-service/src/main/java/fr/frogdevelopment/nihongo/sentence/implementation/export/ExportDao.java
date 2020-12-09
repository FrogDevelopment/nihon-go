package fr.frogdevelopment.nihongo.sentence.implementation.export;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ExportDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ExportDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> call(String lang) {
        var sql = """
                SELECT JSON_BUILD_OBJECT(
                      'linking', i.linking,
                      'japanese', j.sentence,
                      'translations', ARRAY_AGG(t.sentence))
                FROM sentences j
                        INNER JOIN links_japanese_translation l ON l.japanese_id = j.sentence_id
                        INNER JOIN sentences t ON t.sentence_id = l.translation_id AND t.lang = :lang
                        INNER JOIN japanese_indices i ON i.japanese_id = j.sentence_id
                GROUP BY i.linking, j.sentence
                """;

        var params = new MapSqlParameterSource("lang", lang);

        return namedParameterJdbcTemplate.queryForList(sql, params, String.class);
    }
}
