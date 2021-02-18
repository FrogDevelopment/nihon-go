package com.frogdevelopment.nihongo.sentences.implementation.populate;

import static org.mockito.BDDMockito.then;

import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.frogdevelopment.nihongo.export.ExportData;
import com.frogdevelopment.nihongo.sentences.implementation.about.AboutDao;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class FetchSentencesTest {

    @InjectMocks
    private FetchSentences fetchSentences;

    @Mock
    private PopulateDatabase populateDatabase;
    @Mock
    private AboutDao aboutDao;
    @Mock
    private DeleteDownloadedFiles deleteDownloadedFiles;
    @Mock
    private ExportData exportData;

    @Test
    void shouldCallFetcher() {
        // when
        fetchSentences.call();

        // then
        then(populateDatabase).should().call();
        then(aboutDao).should().generate(Map.of());
        then(deleteDownloadedFiles).should().call();
        then(exportData).should().call();
    }

}
