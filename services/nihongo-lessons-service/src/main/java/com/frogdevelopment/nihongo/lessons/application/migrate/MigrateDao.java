package com.frogdevelopment.nihongo.lessons.application.migrate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Map;

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
    public int insertJapanese(final Map<String, Object> japanese) {
        final var key = japaneseJdbcInsert.executeAndReturnKey(japanese);

        return key.intValue();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void insertTranslation(final Map<String, Object> translations) {
        translationJdbcInsert.execute(translations);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void insertExportableLessons(final int lesson, final String locale) {
        jdbcTemplate.update("""
                INSERT INTO exportable_lessons(lesson, locale, exportable, update_datetime)
                VALUES (?, ?, TRUE, NOW())
                """, lesson, locale);
    }
}
