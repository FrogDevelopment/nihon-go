package com.frogdevelopment.nihongo.lessons.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;

import static javax.transaction.Transactional.TxType.MANDATORY;

@Slf4j
@Transactional(MANDATORY)
@RequiredArgsConstructor
@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class InputDao implements GenericRepository<InputDto, Integer> {

    private final JdbcOperations jdbcOperations;
    private final ObjectMapper objectMapper;

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
                 WHERE j.lesson = ?
                 GROUP BY j.japanese_id, j.kanji, j.kana, j.lesson
                 ORDER BY j.%s %s""".formatted(sortField, sortOrder);

        return jdbcOperations.prepareStatement(sqlJapanese, statement -> {
            statement.setInt(1, lesson);

            final var resultSet = statement.executeQuery();

            var results = new ArrayList<InputDto>();

            while (resultSet.next()) {
                try {
                    results.add(objectMapper.readValue(resultSet.getString("json_build_object"), InputDto.class));
                } catch (JsonProcessingException e) {
                    log.error("Failed to read json value", e);
                }
            }

            return results;
        });
    }
}
