package fr.frogdevelopment.nihongo.sentence.implementation.about;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Slf4j
@Repository
public class AboutDao {

    private final JdbcTemplate jdbcTemplate;

    public AboutDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(propagation = REQUIRED)
    public void generate() {
        var sql = """
                WITH nb_sentences AS (SELECT COUNT(*) count_entries FROM sentences),
                     count_by_lang AS (WITH count_by_lang AS (SELECT lang, COUNT(lang)::text AS count_lang
                                                              FROM sentences
                                                              GROUP BY lang
                                                              ORDER BY COUNT(lang) DESC)
                                       SELECT JSON_OBJECT(ARRAY_AGG(lang), ARRAY_AGG(count_lang)) FROM count_by_lan
                )
                                  
                INSERT INTO about(date_import, nb_entries, languages)
                SELECT NOW(), *
                FROM nb_sentences, count_by_lang;
                """;

        jdbcTemplate.update(sql);
    }

    @Transactional(propagation = REQUIRED)
    public String getLast() {
        String sql = """
                SELECT JSON_BUILD_OBJECT('date_import', date_import, 'nb_entries', nb_entries, 'languages', languages)
                FROM about
                ORDER BY about_id DESC
                LIMIT 1
                """;

        try {
            return jdbcTemplate.queryForObject(sql, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "no data";
        }
    }

    @Transactional(propagation = REQUIRED)
    public String getLanguages() {
        String sql = """
                SELECT languages
                FROM about
                ORDER BY about_id
                DESC LIMIT 1
                """;

        try {
            return jdbcTemplate.queryForObject(sql, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "no data";
        }
    }

}
