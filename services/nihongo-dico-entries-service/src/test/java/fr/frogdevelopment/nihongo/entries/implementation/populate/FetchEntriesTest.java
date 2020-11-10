package fr.frogdevelopment.nihongo.entries.implementation.populate;

import static org.mockito.BDDMockito.given;

import fr.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import fr.frogdevelopment.nihongo.entries.implementation.export.ExportByLang;
import fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.FetchJMDict;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class FetchEntriesTest {

    @InjectMocks
    private FetchEntries fetchEntries;

    @Mock
    private FetchJMDict fetchJmDict;
    @Mock
    private SaveData saveData;
    @Mock
    private AboutDao aboutDao;
    @Mock
    private DeleteDownloadedFiles deleteDownloadedFiles;
    @Mock
    private ExportByLang exportByLang;

    @Test
    void shouldCallFetcher() throws IOException, URISyntaxException {
        // given
        given(fetchJmDict
                .execute())
                .willReturn("2015-06-18");

        // when
        fetchEntries.call();

        // then
        var inOrder = Mockito.inOrder(fetchJmDict, saveData, aboutDao, deleteDownloadedFiles, exportByLang);
        inOrder.verify(fetchJmDict).execute();
        inOrder.verify(saveData).call();
        inOrder.verify(aboutDao).insert("2015-06-18");
        inOrder.verify(deleteDownloadedFiles).call();
        inOrder.verify(exportByLang).call();
    }

}
