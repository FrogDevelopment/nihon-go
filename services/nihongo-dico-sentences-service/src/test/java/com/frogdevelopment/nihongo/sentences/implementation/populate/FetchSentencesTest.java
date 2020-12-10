package com.frogdevelopment.nihongo.sentences.implementation.populate;

import com.frogdevelopment.nihongo.sentences.implementation.about.AboutDao;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.BDDMockito.then;

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

    @Test
    void shouldCallFetcher() {
        // when
        fetchSentences.call();

        // then
        then(populateDatabase).should().call();
        then(aboutDao).should().generate(Map.of());
        then(deleteDownloadedFiles).should().call();
    }

}
