package com.frogdevelopment.nihongo.lessons.implementation.export;

import com.frogdevelopment.nihongo.export.ExportData;
import com.frogdevelopment.nihongo.export.ExportData.Export;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportLessons {

    @Language("SQL")
    private static final String SQL_LESSONS = """
            SELECT lesson, locale
            FROM exportable_lessons
            WHERE exportable = true;
            """;

    @Language("SQL")
    private static final String SQL_COPY = """
            COPY ( SELECT j.kanji,
                       j.kana,
                       t.sort_letter,
                       t.input,
                       t.details,
                       t.example,
                       ARRAY_TO_STRING(t.tags,',') AS tags
                 FROM japaneses j
                 INNER JOIN translations t
                            ON j.japanese_id = t.japanese_id
                            AND t.locale = '%1$s'
                            AND lesson = %2$s)
            TO STDOUT;
            """;

    @Language("SQL")
    private static final String SQL_COPY_EXPORTABLE = """
            COPY ( SELECT json_build_object( 
            'lesson', lesson,
            'locale', locale,
            'update_datetime', update_datetime
            )
                 FROM exportable_lessons
                 WHERE exportable = true)
            TO STDOUT WITH (FORMAT CSV, HEADER);
            """;

    private final ExportData exportData;
    private final JdbcOperations jdbcOperations;

    public void call() {
        final var exports = jdbcOperations.query(SQL_LESSONS, (rs, rowNum) -> toExport(rs));
        exportData.call(exports.stream());
    }

    private Export toExport(final ResultSet rs) throws SQLException {
        final var locale = rs.getString("locale");
        final var lesson = String.format("%02d", rs.getInt("lesson"));
        return Export.of(locale + "-" + lesson, SQL_COPY.formatted(locale, lesson));
    }
}
