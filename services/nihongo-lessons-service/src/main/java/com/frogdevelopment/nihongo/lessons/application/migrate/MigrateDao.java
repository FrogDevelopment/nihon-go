package com.frogdevelopment.nihongo.lessons.application.migrate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
class MigrateDao {

    private final SimpleJdbcInsert japaneseJdbcInsert;
    private final SimpleJdbcInsert translationJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    MigrateDao(final DataSource dataSource) {
        japaneseJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("japaneses")
                .usingGeneratedKeyColumns("japanese_id");
        translationJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("translations")
                .usingGeneratedKeyColumns("translation_id");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    void insertWord(final Map<String, Object> japanese, final List<Map<String, Object>> translations) {
        final var key = japaneseJdbcInsert.executeAndReturnKey(japanese);
        final var japaneseId = key.intValue();

        translations
                .stream()
                .filter(translation -> !translation.isEmpty())
                .peek(translation -> translation.put("japanese_id", japaneseId))
                .forEach(translationJdbcInsert::execute);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    void insertExportableLessons(final int lesson, final String locale) {
        jdbcTemplate.update("""
                                    INSERT INTO exportable_lessons(lesson, locale, exportable, update_datetime)
                                    VALUES (?, ?, true, now())
                                    """, lesson, locale);
    }
}
