package fr.frogdevelopment.nihongo.sentence.implementation.populate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RefreshScope
public class PopulateDatabase {

    @Value("${frog.tatoeba.url.japanese-indices}")
    private String japaneseIndicesUrl;
    @Value("${frog.tatoeba.url.links}")
    private String linksUrl;
    @Value("${frog.tatoeba.url.sentences}")
    private String sentencesUrl;

    private final DownloadFile downloadFile;
    private final ClearImportTable clearImportTable;
    private final CopyFromPath copyFromPath;
    private final DataSource dataSource;
    private final SentencesDao sentencesDao;

    public PopulateDatabase(DownloadFile downloadFile,
                            ClearImportTable clearImportTable,
                            CopyFromPath copyFromPath,
                            DataSource dataSource,
                            SentencesDao sentencesDao) {
        this.downloadFile = downloadFile;
        this.clearImportTable = clearImportTable;
        this.copyFromPath = copyFromPath;
        this.dataSource = dataSource;
        this.sentencesDao = sentencesDao;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void call() {
        try {
            log.info(">>>>>>>>>> Download required files");
            log.info("****** Fetch tatoeba sentence file");
            var sentencesPath = downloadFile.call(sentencesUrl);

            log.info("****** Fetch links between tatoeba sentence files");
            var linksPath = downloadFile.call(linksUrl);

            log.info("****** Fetch links between JMDict & Tatoeba sentence files");
            var indicesPath = downloadFile.call(japaneseIndicesUrl);

            log.info(">>>>>>>>>> Start populating database");
            try (var connection = dataSource.getConnection()) {

                log.info("****** clearing tables import");
                clearImportTables(connection);

                log.info("****** copy tatoeba sentence");
                copyFromPath.call(connection, sentencesPath, "i_sentences", "sentence_id, lang, sentence");

                log.info("****** copy links between tatoeba sentence");
                copyFromPath.call(connection, linksPath, "i_links", "sentence_id, translation_id");

                log.info("****** copy links between JMDict & Tatoeba sentence");
                copyFromPath.call(connection, indicesPath, "i_japanese_indices", "sentence_id, meaning_id, linking");

                log.info("****** cleaning imported data");
                sentencesDao.prepareImportedData(connection);

                log.info("****** construct final data");
                sentencesDao.insertSentences(connection);
                sentencesDao.insertTranslationsLinks(connection);
                sentencesDao.insertJapaneseIndices(connection);

                log.info("****** clearing tables import");
                clearImportTables(connection);
            }
        } catch (SQLException | IOException | URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    private void clearImportTables(Connection connection) throws SQLException {
        clearImportTable.call(connection, "i_japanese_indices");
        clearImportTable.call(connection, "i_sentences");
        clearImportTable.call(connection, "i_links");
    }

}
