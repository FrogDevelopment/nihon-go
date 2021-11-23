package com.frogdevelopment.nihongo.lessons.application.migrate;

import static com.frogdevelopment.nihongo.lessons.Utils.getSortLetter;
import static java.util.stream.IntStream.rangeClosed;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
        final var english = new HashMap<String, Object>(5);
        final var french = new HashMap<String, Object>(5);

        try (final var in = new BufferedInputStream(new URL(url).openStream());
             final var reader = new InputStreamReader(in);
             final var parse = CSVFormat.TDF
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(reader)) {

            for (final var csvRecord : parse.getRecords()) {
                japanese.clear();
                japanese.put("kanji", trimToNull(csvRecord.get("kanji")));
                japanese.put("kana", trim(csvRecord.get("kana")));
                japanese.put("lesson", Integer.valueOf(csvRecord.get("tags")));

                fillMap(english, "en_US", csvRecord);
                fillMap(french, "fr_FR", csvRecord);

                migrateDao.insertWord(japanese, List.of(english, french));
            }

            rangeClosed(1, 21).forEach(value -> migrateDao.insertExportableLessons(value, "en_US"));
            rangeClosed(1, 21).forEach(value -> migrateDao.insertExportableLessons(value, "fr_FR"));

        } catch (final IOException e) {
            log.error("Error while fetching lesson", e);
        }
        log.info("--- Migrate - end");
    }

    private void fillMap(final Map<String, Object> map, final String locale, final CSVRecord csvRecord) {
        final var input = capitalize(csvRecord.get(locale + "_input"));
        if (isNotBlank(input)) {
            map.put("locale", locale);
            map.put("input", input);
            map.put("sort_letter", getSortLetter(input));
            map.put("details", trimToNull(csvRecord.get(locale + "_details")));
            map.put("example", trimToNull(csvRecord.get(locale + "_example")));
        } else {
            map.clear();
        }
    }
}
