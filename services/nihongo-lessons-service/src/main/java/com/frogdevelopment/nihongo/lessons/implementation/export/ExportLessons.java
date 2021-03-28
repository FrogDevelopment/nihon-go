package com.frogdevelopment.nihongo.lessons.implementation.export;

import com.frogdevelopment.nihongo.export.ExportData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.frogdevelopment.nihongo.export.ExportData.Export.of;
import static java.util.stream.IntStream.rangeClosed;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportLessons {

    private static final Set<String> LOCALES = Set.of("fr_FR", "en_US"); // fixme dynamic

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
                            AND ARRAY_TO_STRING(t.tags,',') LIKE '%%%2$s%%')
            TO STDOUT WITH (FORMAT CSV, HEADER);
            """;

    private final ExportData exportData;
    private final Environment environment;

    public void call() {
        exportData.call(LOCALES.stream()
                                .flatMap(locale -> rangeClosed(1, getAvailableLessons(locale))
                                        .mapToObj(v -> String.format("%02d", v))
                                        .map(numberIn2Digits -> of(locale + "-" + numberIn2Digits, SQL_COPY.formatted(locale, numberIn2Digits)))));
    }

    private Integer getAvailableLessons(final String locale) {
        return environment.getRequiredProperty("frog.lessons.last_ready." + locale, Integer.class);
    }
}
