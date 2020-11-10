package fr.frogdevelopment.nihongo.entries.implementation.about;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AboutDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AboutDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional(propagation = REQUIRED)
    public void insert(String date) {
        var sql = "WITH nb_entries AS (SELECT COUNT(*) count_entries FROM entries), "
                  + "     count_by_lang AS (WITH count_by_lang AS (SELECT lang, COUNT(lang)::text AS count_lang "
                  + "                                              FROM glosses "
                  + "                                              GROUP BY lang "
                  + "                                              ORDER BY COUNT(lang) DESC) "
                  + "                       SELECT JSON_OBJECT(ARRAY_AGG(lang), ARRAY_AGG(count_lang)) FROM count_by_lang"
                  + ") "
                  + " "
                  + "INSERT INTO about(jmdict_date, nb_entries, languages) "
                  + "SELECT :jmdict_date, * "
                  + "FROM nb_entries, count_by_lang;";

        var paramSource = new MapSqlParameterSource();
        paramSource.addValue("jmdict_date", date);
        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    @Transactional(propagation = REQUIRED)
    public String getLast() {
        var sql =
                "SELECT JSON_BUILD_OBJECT('jmdict_date', jmdict_date, 'nb_entries', nb_entries, 'languages', languages) "
                + "FROM about "
                + "ORDER BY about_id DESC "
                + "LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "no data";
        }
    }

    @Transactional(propagation = REQUIRED)
    public String getLanguages() {
        String sql = "SELECT languages" +
                     " FROM about" +
                     " ORDER BY about_id" +
                     " DESC LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "no data";
        }
    }

}
