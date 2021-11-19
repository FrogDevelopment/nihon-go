package com.frogdevelopment.nihongo.lessons.application.migrate;

import static com.frogdevelopment.nihongo.lessons.Utils.getSortLetter;
import static java.util.stream.IntStream.rangeClosed;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OldMigrateLessons {

    private static final String COL_INPUT_EN_US = "en_US_input";
    private static final String COL_DETAILS_EN_US = "en_US_details";
    private static final String COL_EXAMPLE_EN_US = "en_US_example";
    private static final String COL_INPUT_FR_FR = "fr_FR_input";
    private static final String COL_DETAILS_FR_FR = "fr_FR_details";
    private static final String COL_EXAMPLE_FR_FR = "fr_FR_example";

    private final String url;
    private final MigrateDao migrateDao;

    public OldMigrateLessons(@Value("${frog.migrate.old.url}") final String url,
                             final MigrateDao migrateDao) {
        this.url = url;
        this.migrateDao = migrateDao;
    }

    @Transactional(propagation = REQUIRED)
    public void call() {
        log.info("--- Migrate - start");
        final var japanese = new HashMap<String, Object>(3);
        final var english = new HashMap<String, Object>(6);
        final var french = new HashMap<String, Object>(6);

        try (final var in = new BufferedInputStream(new URL(url).openStream());
             final var reader = new InputStreamReader(in);
             final var parse = CSVFormat.TDF
                     .withHeader()
                     .withSkipHeaderRecord()
                     .parse(reader)) {

            final var now = LocalDateTime.now();
            for (final var record : parse.getRecords()) {
                japanese.clear();
                japanese.put("kanji", record.get("kanji"));
                japanese.put("kana", record.get("kana"));
                japanese.put("created_date", now);
                japanese.put("modified_date", now);
                japanese.put("created_by", "old_migrate");
                japanese.put("modified_by", "old_migrate");

                english.put("locale", "en_US");
                fillMap(now, english, COL_INPUT_EN_US, COL_DETAILS_EN_US, COL_EXAMPLE_EN_US, record);

                french.put("locale", "fr_FR");
                fillMap(now, french, COL_INPUT_FR_FR, COL_DETAILS_FR_FR, COL_EXAMPLE_FR_FR, record);

                migrateDao.insertWord(japanese, List.of(english, french));
            }

            rangeClosed(1, 21).forEach(value -> migrateDao.insertExportableLessons(value, "en_US"));
            rangeClosed(1, 21).forEach(value -> migrateDao.insertExportableLessons(value, "fr_FR"));

        } catch (final IOException e) {
            log.error("Error while fetching lesson", e);
        }
        log.info("--- Migrate - end");
    }

    private void fillMap(final LocalDateTime now, final Map<String, Object> map, final String col_input, final String col_details, final String col_example,
                         final CSVRecord record) {
        final var input = capitalize(record.get(col_input));
        if (isNotBlank(input)) {
            map.put("input", input);
            map.put("sort_letter", getSortLetter(input));
            map.put("details", trimToNull(record.get(col_details)));
            map.put("example", trimToNull(record.get(col_example)));
            map.put("lesson", record.get("tags"));
            map.put("created_date", now);
            map.put("modified_date", now);
            map.put("created_by", "old_migrate");
            map.put("modified_by", "old_migrate");
        } else {
            map.clear();
        }
    }
}
