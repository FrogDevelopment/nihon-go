package com.frogdevelopment.nihongo.lessons.application.export;

import com.frogdevelopment.nihongo.export.ExportData;
import com.frogdevelopment.nihongo.export.ExportData.Export;
import com.frogdevelopment.nihongo.lessons.application.ExportLessons;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ExportLessonsImpl implements ExportLessons {

    @Language("SQL")
    private static final String SQL_LOCALES = """
            SELECT DISTINCT t.locale
            FROM lessons l
            INNER JOIN japaneses j ON l.lesson = j.lesson
            INNER JOIN translations t ON j.japanese_id = t.japanese_id
            WHERE l.lesson = ?
            """;

    @Language("SQL")
    private static final String SQL_COPY = """
            COPY (SELECT j.kanji,
                       j.kana,
                       t.sort_letter,
                       t.input,
                       t.details,
                       t.example
                 FROM japaneses j
                 INNER JOIN translations t
                            ON j.japanese_id = t.japanese_id
                            AND t.locale = '%1$s'
                            AND lesson = %2$s)
            TO STDOUT;
            """;

    private final ExportData exportData;
    private final JdbcOperations jdbcOperations;
    private final LessonDao lessonDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void call(final int lesson) {
        try (final var exports = jdbcOperations.queryForStream(SQL_LOCALES, getRowMapper(lesson), lesson)) {
            exportData.call(exports);
        }

        lessonDao.exportLesson(lesson);
    }

    private RowMapper<Export> getRowMapper(final int lesson) {
        return (rs, rowNum) -> {
            final String locale = rs.getString("locale");
            return Export.of(locale + "-" + lesson, SQL_COPY.formatted(locale, lesson));
        };
    }
}
