package com.frogdevelopment.nihongo.entries.implementation.about;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Repository
@RequiredArgsConstructor
public class AboutDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional(propagation = REQUIRED)
    public void insert(final String date, final Map<String, Object> data) {
        final var sql = "INSERT INTO about(jmdict_date, nb_entries, languages) VALUES (:jmdict_date, :nb_entries, :languages);";

        final var paramSource = new MapSqlParameterSource();
        paramSource.addValue("jmdict_date", date);
        paramSource.addValue("nb_entries", data.get("nb_entries"));
        try {
            final var jsonbObj = new PGobject();
            jsonbObj.setType("json");
            jsonbObj.setValue(new ObjectMapper().writeValueAsString(data.get("languages")));
            paramSource.addValue("languages", jsonbObj, Types.OTHER);
        } catch (final SQLException | JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    @Transactional(propagation = REQUIRED)
    public String getLast() {
        final var sql = """
                SELECT JSON_BUILD_OBJECT('jmdict_date', jmdict_date, 'nb_entries', nb_entries, 'languages', languages)
                FROM about
                ORDER BY about_id DESC
                LIMIT 1
                """;

        try {
            return namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, String.class);
        } catch (final EmptyResultDataAccessException e) {
            return "no data";
        }
    }

    @Transactional(propagation = REQUIRED)
    public String getLanguages() {
        final String sql = """
                SELECT languages
                FROM about
                ORDER BY about_id
                DESC LIMIT 1
                """;

        try {
            return namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, String.class);
        } catch (final EmptyResultDataAccessException e) {
            return "no data";
        }
    }

}
