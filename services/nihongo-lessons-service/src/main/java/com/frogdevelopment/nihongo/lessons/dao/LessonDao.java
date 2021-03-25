package com.frogdevelopment.nihongo.lessons.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class LessonDao {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Input> getLesson(final String locale, final String lesson) {
        final var sql = "SELECT j.kanji,"
                + "       j.kana,"
                + "       t.sort_letter,"
                + "       t.input,"
                + "       t.details,"
                + "       t.example,"
                + "       ARRAY_TO_STRING(t.tags,',') AS tags"
                + " FROM japaneses j"
                + "         INNER JOIN translations t"
                + "                    ON j.japanese_id = t.japanese_id"
                + "                        AND t.locale = :locale"
                  + "                        AND ARRAY_TO_STRING(t.tags,',') LIKE :lesson;";

        final var paramSource = new MapSqlParameterSource();
        paramSource.addValue("locale", locale);
        paramSource.addValue("lesson", "%" + lesson + "%");

        final var builder = Input.builder();
        return namedParameterJdbcTemplate.query(sql, paramSource, (rs, rowNum) -> builder
                .kanji(rs.getString("kanji"))
                .kana(rs.getString("kana"))
                .sortLetter(rs.getString("sort_letter"))
                .input(rs.getString("input"))
                .details(rs.getString("details"))
                .example(rs.getString("example"))
                .tags(rs.getString("tags"))
                .build());
    }

    public Integer getTotal() {
        final var sql = "SELECT COUNT(j.japanese_id) FROM japaneses j;";

        return namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    public List<InputDto> fetch(final int pageIndex, final int pageSize, final String sortField, final String sortOrder) {
        final var sqlJapanese = "SELECT json_build_object("
                + "               'japanese', json_build_object("
                + "                'id', j.japanese_id,"
                + "                'kanji', j.kanji,"
                + "                'kana', j.kana),"
                + "               'translations', array_agg(json_build_object("
                + "                'id', t.translation_id,"
                + "                'japaneseId', t.japanese_id,"
                + "                'locale', t.locale,"
                + "                'input', t.input,"
                + "                'sortLetter', t.sort_letter,"
                          + "                'details', t.details,"
                          + "                'example', t.example,"
                          + "                'tags', t.tags"
                          + "            ))"
                          + "           )"
                          + " FROM japaneses j"
                          + "    INNER JOIN translations t ON t.japanese_id = j.japanese_id"
                          + " GROUP BY j.japanese_id, j.kanji, j.kana"
                          + " ORDER BY j." + sortField + " " + sortOrder
                          + " LIMIT :pageSize OFFSET :offset";

        final var paramSource = new MapSqlParameterSource();
        paramSource.addValue("pageSize", pageSize);
        paramSource.addValue("offset", (pageIndex - 1) * pageSize);

        return namedParameterJdbcTemplate.query(sqlJapanese, paramSource, (rs, rowNum) -> {
            final var json = rs.getString("json_build_object");
            try {
                return objectMapper.readValue(json, InputDto.class);
            } catch (final IOException e) {
                log.error("Failed to read json value", e);
                return null;
            }
        });
    }

    public List<String> getTags() {
        final var sql = "SELECT DISTINCT UNNEST(tags) FROM translations ORDER BY UNNEST(tags)";

        return namedParameterJdbcTemplate.getJdbcTemplate().queryForList(sql, String.class);
    }

}
