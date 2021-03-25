package com.frogdevelopment.nihongo.lessons.migrate.implementation;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
class MigrateDao {

    private final SimpleJdbcInsert japaneseJdbcInsert;
    private final SimpleJdbcInsert translationJdbcInsert;

    MigrateDao(final DataSource dataSource) {
        japaneseJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("japaneses")
                .usingGeneratedKeyColumns("japanese_id");
        translationJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("translations")
                .usingGeneratedKeyColumns("translation_id");
    }

    @Transactional(propagation = Propagation.MANDATORY)
    void insertWord(final Map<String, String> japanese, final List<Map<String, Object>> translations) {
        final var key = japaneseJdbcInsert.executeAndReturnKey(japanese);
        final var japaneseId = key.intValue();

        translations
                .stream()
                .filter(translation -> !translation.isEmpty())
                .peek(translation -> translation.put("japanese_id", japaneseId))
                .forEach(translationJdbcInsert::execute);
    }
}
