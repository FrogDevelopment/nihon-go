package com.frogdevelopment.nihongo.lessons.application.migrate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import javax.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.frogdevelopment.nihongo.ftp.FtpClientHelper;
import com.frogdevelopment.nihongo.lessons.dao.JapaneseDao;
import com.frogdevelopment.nihongo.lessons.dao.TranslationDao;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;
import com.frogdevelopment.nihongo.lessons.entity.Translation;

import jakarta.inject.Singleton;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class OldMigrateLessons {

    private static final String SOURCE = "nihon_go/NihonGo_All.tsv";
    private static final String[] LOCALES = { "en_US", "fr_FR" };

    private final FtpClientHelper ftpClient;
    private final JapaneseDao japaneseDao;
    private final TranslationDao translationDao;

    @Transactional(REQUIRED)
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
                    final var japaneseId = insertJapanese(csvRecord);
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

    private Long insertJapanese(final CSVRecord csvRecord) {
        final var japanese = Japanese.builder()
                .kanji(csvRecord.get("kanji"))
                .kana(csvRecord.get("kana"))
                .lesson(Integer.valueOf(csvRecord.get("tags")))
                .build();

        return japaneseDao.save(japanese).getId();
    }

    private void insertTranslation(final Long japaneseId, final CSVRecord csvRecord) {
        final var translationBuilder = Translation.builder()
                .japaneseId(japaneseId);
        for (final String locale : LOCALES) {
            final var input = capitalize(csvRecord.get(locale + "_input"));
            if (isNotBlank(input)) {
                translationBuilder.locale(locale);
                translationBuilder.input(input);
                translationBuilder.details(csvRecord.get(locale + "_details"));
                translationBuilder.example(csvRecord.get(locale + "_example"));

                translationDao.save(translationBuilder.build());
            }
        }
    }
}
