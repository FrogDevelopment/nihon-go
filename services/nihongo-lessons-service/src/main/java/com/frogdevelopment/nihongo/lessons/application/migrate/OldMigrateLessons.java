package com.frogdevelopment.nihongo.lessons.application.migrate;

import com.frogdevelopment.nihongo.ftp.FtpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import static com.frogdevelopment.nihongo.lessons.Utils.getSortLetter;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Slf4j
@Component
@RequiredArgsConstructor
public class OldMigrateLessons {

    private static final String SOURCE = "nihon_go/NihonGo_All.tsv";
    private static final String[] LOCALES = {"en_US", "fr_FR"};

    private final FtpClient ftpClient;
    private final SimpleJdbcInsert japaneseJdbcInsert;
    private final SimpleJdbcInsert translationJdbcInsert;

    @Transactional(propagation = REQUIRED)
    public void call() {
        log.info("--- Migrate - start");
        try {
            ftpClient.open();

            try (final var in = ftpClient.retrieveFileStream(SOURCE);
                 final var reader = new InputStreamReader(in);
                 final var parse = CSVFormat.TDF
                         .builder()
                         .setHeader()
                         .setSkipHeaderRecord(true)
                         .build()
                         .parse(reader)) {

                for (final var csvRecord : parse.getRecords()) {
                    final int japaneseId = insertJapanese(csvRecord);
                    insertTranslation(japaneseId, csvRecord);
                }
            } finally {
                ftpClient.close();
                log.info("--- Migrate - end");
            }
        } catch (final IOException e) {
            log.error("Error while fetching lesson", e);
        }
    }

    private int insertJapanese(final CSVRecord csvRecord) {
        final var japanese = new HashMap<String, Object>(3);
        japanese.put("kanji", trimToNull(csvRecord.get("kanji")));
        japanese.put("kana", trim(csvRecord.get("kana")));
        japanese.put("lesson", Integer.valueOf(csvRecord.get("tags")));

        return japaneseJdbcInsert.executeAndReturnKey(japanese).intValue();
    }

    private void insertTranslation(final int japaneseId, final CSVRecord csvRecord) {
        final var translation = new HashMap<String, Object>(6);
        translation.put("japanese_id", japaneseId);
        for (final String locale : LOCALES) {
            final var input = capitalize(csvRecord.get(locale + "_input"));
            if (isNotBlank(input)) {
                translation.put("locale", locale);
                translation.put("input", input);
                translation.put("sort_letter", getSortLetter(input));
                translation.put("details", trimToNull(csvRecord.get(locale + "_details")));
                translation.put("example", trimToNull(csvRecord.get(locale + "_example")));

                translationJdbcInsert.execute(translation);
            }
        }
    }
}
