package com.frogdevelopment.nihongo.lessons.migrate.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Slf4j
@Component
@RequiredArgsConstructor
public class MigrateLessons {

    private static final String BASE_URL = "http://legall.benoit.free.fr/nihon_go/";
    private static final String LESSONS_FILE = "NihonGo_All.tsv";
    private static final String URL = BASE_URL + LESSONS_FILE;

    private static final String COL_INPUT_EN_US = "en_US_input";
    private static final String COL_DETAILS_EN_US = "en_US_details";
    private static final String COL_EXAMPLE_EN_US = "en_US_example";
    private static final String COL_INPUT_FR_FR = "fr_FR_input";
    private static final String COL_DETAILS_FR_FR = "fr_FR_details";
    private static final String COL_EXAMPLE_FR_FR = "fr_FR_example";

    private final MigrateDao migrateDao;

    @Transactional(propagation = REQUIRED)
    public void call() {
        log.info("--- Migrate - start");
        final var japanese = new HashMap<String, String>(3);
        final var english = new HashMap<String, Object>(6);
        final var french = new HashMap<String, Object>(6);

        try (final var in = new BufferedInputStream(new URL(URL).openStream());
             final var reader = new InputStreamReader(in);
             final var parse = CSVFormat.TDF.withHeader().withSkipHeaderRecord().parse(reader)) {

            for (final var record : parse.getRecords()) {
                japanese.clear();
                japanese.put("kanji", record.get("kanji"));
                japanese.put("kana", record.get("kana"));

                english.put("locale", "en_US");
                fillMap(english, COL_INPUT_EN_US, COL_DETAILS_EN_US, COL_EXAMPLE_EN_US, record);

                french.put("locale", "fr_FR");
                fillMap(french, COL_INPUT_FR_FR, COL_DETAILS_FR_FR, COL_EXAMPLE_FR_FR, record);

                migrateDao.insertWord(japanese, List.of(english, french));
            }

        } catch (final IOException e) {
            log.error("Error while fetching lesson", e);
        }
        log.info("--- Migrate - end");
    }

    private void fillMap(final Map<String, Object> map, final String col_input, final String col_details, final String col_example,
                         final CSVRecord record) {
        final var input = capitalize(record.get(col_input));
        if (isNotBlank(input)) {
            map.put("input", input);

            // Normalizer.normalize(source, Normalizer.Form.NFD) renvoi une chaine unicode décomposé.
            // C'est à dire que les caractères accentués seront décomposé en deux caractères (par exemple "à" se transformera en "a`").
            // Le replaceAll("[\u0300-\u036F]", "") supprimera tous les caractères unicode allant de u0300 à u036F,
            // c'est à dire la plage de code des diacritiques (les accents qu'on a décomposé ci-dessus donc).
            final var sortLetter = Normalizer.normalize(input.substring(0, 1), Normalizer.Form.NFD)
                    .replaceAll("[\u0300-\u036F]", "");

            map.put("sort_letter", sortLetter.toUpperCase());
            map.put("details", trimToNull(record.get(col_details)));
            map.put("example", trimToNull(record.get(col_example)));
            var tags = record.get("tags");
            if (map.get("locale").equals("fr_FR")) {
                tags = "Leçon " + tags;
            } else {
                tags = "Lesson " + tags;
            }
            map.put("tags", ArrayUtils.add(null, tags));
        } else {
            map.clear();
        }
    }
}
