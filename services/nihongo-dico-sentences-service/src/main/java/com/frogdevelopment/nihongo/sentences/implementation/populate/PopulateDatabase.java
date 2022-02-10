package com.frogdevelopment.nihongo.sentences.implementation.populate;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.context.scope.Refreshable;
import jakarta.inject.Singleton;

import static javax.transaction.Transactional.TxType.REQUIRED;

@Slf4j
@Singleton
@Refreshable
public class PopulateDatabase {

    @Value("${frog.tatoeba.url.japanese-indices}")
    private String japaneseIndicesUrl;
    @Value("${frog.tatoeba.url.links}")
    private String linksUrl;
    @Value("${frog.tatoeba.url.sentences}")
    private String sentencesUrl;

    private final DownloadFile downloadFile;
    private final CreateTemporaryImportTables createTemporaryImportTables;
    private final CopyFromPath copyFromPath;
    private final DataSource dataSource;
    private final SentencesDao sentencesDao;

    public PopulateDatabase(final DownloadFile downloadFile,
                            final CreateTemporaryImportTables createTemporaryImportTables,
                            final CopyFromPath copyFromPath,
                            final DataSource dataSource,
                            final SentencesDao sentencesDao) {
        this.downloadFile = downloadFile;
        this.createTemporaryImportTables = createTemporaryImportTables;
        this.copyFromPath = copyFromPath;
        this.dataSource = dataSource;
        this.sentencesDao = sentencesDao;
    }

    @Transactional(REQUIRED)
    public Map<String, Integer> call() {
        try {
            log.info(">>>>>>>>>> Download required files");
            log.info("****** Fetch tatoeba sentence file");
            final var sentencesPath = downloadFile.call(sentencesUrl);

            log.info("****** Fetch links between tatoeba sentence files");
            final var linksPath = downloadFile.call(linksUrl);

            log.info("****** Fetch links between JMDict & Tatoeba sentence files");
            final var indicesPath = downloadFile.call(japaneseIndicesUrl);

            log.info(">>>>>>>>>> Start populating database");
            try (final var connection = dataSource.getConnection()) {

                log.info("****** creating temporary import tables");
                createTemporaryImportTables.call(connection);

                log.info("****** copy tatoeba sentences");
                copyFromPath.call(connection, sentencesPath, "tmp_sentences", "sentence_id, lang, sentence");

                log.info("****** copy links between tatoeba sentences");
                copyFromPath.call(connection, linksPath, "tmp_links", "sentence_id, translation_id");

                log.info("****** copy links between JMDict & Tatoeba sentences");
                copyFromPath.call(connection, indicesPath, "tmp_japanese_indices", "sentence_id, meaning_id, linking");

                log.info("****** cleaning imported data");
                sentencesDao.prepareImportedData(connection);

                log.info("****** construct final data");
                final var data = sentencesDao.insertSentences(connection);
                sentencesDao.insertTranslationsLinks(connection);
                sentencesDao.insertJapaneseIndices(connection);

                return data;
            }
        } catch (final SQLException | IOException | URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

}
