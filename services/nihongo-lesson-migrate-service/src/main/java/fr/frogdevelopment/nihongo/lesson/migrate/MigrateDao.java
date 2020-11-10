package fr.frogdevelopment.nihongo.lesson.migrate;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MigrateDao {

    private final SimpleJdbcInsert japaneseJdbcInsert;
    private final SimpleJdbcInsert translationJdbcInsert;

    public MigrateDao(DataSource dataSource) {
        japaneseJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("japaneses")
                .usingGeneratedKeyColumns("japanese_id");
        translationJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("translations")
                .usingGeneratedKeyColumns("translation_id");
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void insertWord(Map<String, String> japanese, List<Map<String, Object>> translations) {
        var key = japaneseJdbcInsert.executeAndReturnKey(japanese);
        var japaneseId = key.intValue();

        translations
                .stream()
                .filter(translation -> !translation.isEmpty())
                .peek(translation -> translation.put("japanese_id", japaneseId))
                .forEach(translationJdbcInsert::execute);
    }
}
