package com.frogdevelopment.nihongo.sentences.implementation.about;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Repository
@RequiredArgsConstructor
public class AboutDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional(propagation = REQUIRED)
    public void generate(final Map<String, Integer> data) {
        final var sql = "INSERT INTO about(date_import, languages) VALUES (NOW(), :languages);";

        final var paramSource = new MapSqlParameterSource();
        try {
            final var jsonbObj = new PGobject();
            jsonbObj.setType("json");
            jsonbObj.setValue(new ObjectMapper().writeValueAsString(data));
            paramSource.addValue("languages", jsonbObj, Types.OTHER);
        } catch (final SQLException | JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    @Transactional(propagation = REQUIRED)
    public String getLast() {
        final String sql = """
                SELECT JSON_BUILD_OBJECT('date_import', date_import, 'languages', languages)
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
