package com.frogdevelopment.nihongo.lessons.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.LessonInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class LessonDaoImpl implements LessonDao {

    private final ObjectMapper objectMapper;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final JdbcOperations jdbcOperations;

    private final RowMapper<LessonInfo> lessonInfoRowMapper = (rs, rowNum) -> new LessonInfo(rs.getString("update_datetime"), rs.getString("export_datetime"));

    @Override
    public List<InputDto> fetch(final int lesson, final String sortField, final String sortOrder) {
        final var sqlJapanese = """
                SELECT JSON_BUILD_OBJECT(
                               'japanese', JSON_BUILD_OBJECT(
                                    'id', j.japanese_id,
                                    'kanji', j.kanji,
                                    'kana', j.kana,
                                    'lesson', j.lesson),
                               'translations', JSON_OBJECT_AGG(
                               t.locale,
                               JSON_BUILD_OBJECT(
                                    'id', t.translation_id,
                                    'japaneseId', t.japanese_id,
                                    'locale', t.locale,
                                    'input', t.input,
                                    'sortLetter', t.sort_letter,
                                    'details', t.details,
                                    'example', t.example
                                ))
                           )
                 FROM japaneses j
                    INNER JOIN translations t ON t.japanese_id = j.japanese_id
                 WHERE j.lesson = :lesson
                 GROUP BY j.japanese_id, j.kanji, j.kana, j.lesson
                 ORDER BY j.%S %s""".formatted(sortField, sortOrder);

        final var paramSource = new MapSqlParameterSource();
        paramSource.addValue("lesson", lesson);

        return namedParameterJdbcOperations.query(sqlJapanese, paramSource, (rs, rowNum) -> {
            final var json = rs.getString("json_build_object");
            try {
                return objectMapper.readValue(json, InputDto.class);
            } catch (final IOException e) {
                log.error("Failed to read json value", e);
                return null;
            }
        });
    }

    @Override
    public void upsertLesson(final int lesson) {
        jdbcOperations.update("INSERT INTO lessons(lesson) VALUES (?) ON CONFLICT (lesson) DO UPDATE SET update_datetime = NOW()", lesson);
    }

    @Override
    public void exportLesson(final int lesson) {
        jdbcOperations.update("UPDATE lessons SET export_datetime = NOW() WHERE lesson = ?", lesson);
    }

    @Override
    public void deleteLesson(final int lesson) {
        jdbcOperations.update("DELETE FROM lessons WHERE lesson = ?", lesson);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<LessonInfo> getLessonInfo(final int lesson) {
        try {
            final var lessonInfo = jdbcOperations.queryForObject("SELECT update_datetime, export_datetime FROM lessons WHERE lesson = ?", lessonInfoRowMapper, lesson);
            return Optional.ofNullable(lessonInfo);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
