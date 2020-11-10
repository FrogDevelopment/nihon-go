package fr.frogdevelopment.nihongo.sentence.implementation.populate;

import static org.mockito.BDDMockito.then;

import fr.frogdevelopment.nihongo.sentence.implementation.about.AboutDao;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        then(aboutDao).should().generate();
        then(deleteDownloadedFiles).should().call();
    }

}
