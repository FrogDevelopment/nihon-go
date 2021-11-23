package com.frogdevelopment.nihongo.lessons.application.migrate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import static com.frogdevelopment.nihongo.lessons.Utils.getSortLetter;
import static java.util.stream.IntStream.rangeClosed;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

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

                final var japaneseId = migrateDao.insertJapanese(japanese);
                fillMap("en_US", japaneseId, csvRecord);
                fillMap("fr_FR", japaneseId, csvRecord);
            }

            rangeClosed(1, 21).forEach(value -> migrateDao.insertExportableLessons(value, "en_US"));
            rangeClosed(1, 21).forEach(value -> migrateDao.insertExportableLessons(value, "fr_FR"));

        } catch (final IOException e) {
            log.error("Error while fetching lesson", e);
        }
        log.info("--- Migrate - end");
    }

    private void fillMap(final String locale, final int japaneseId, final CSVRecord csvRecord) {
        final var input = capitalize(csvRecord.get(locale + "_input"));
        if (isNotBlank(input)) {
            final var translation = new HashMap<String, Object>(6);
            translation.put("japanese_id", japaneseId);
            translation.put("locale", locale);
            translation.put("input", input);
            translation.put("sort_letter", getSortLetter(input));
            translation.put("details", trimToNull(csvRecord.get(locale + "_details")));
            translation.put("example", trimToNull(csvRecord.get(locale + "_example")));

            migrateDao.insertTranslation(translation);
        }
    }
}
