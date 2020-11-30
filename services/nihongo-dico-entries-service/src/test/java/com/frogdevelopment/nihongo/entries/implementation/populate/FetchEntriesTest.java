package com.frogdevelopment.nihongo.entries.implementation.populate;

import com.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.FetchJMDict;
import com.frogdevelopment.nihongo.export.ExportData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import static org.mockito.BDDMockito.given;

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
    private ExportData exportData;

    @Test
    void shouldCallFetcher() throws IOException, URISyntaxException {
        // given
        given(fetchJmDict
                .execute())
                .willReturn("2015-06-18");

        // when
        fetchEntries.call();

        // then
        var inOrder = Mockito.inOrder(fetchJmDict, saveData, aboutDao, deleteDownloadedFiles, exportData);
        inOrder.verify(fetchJmDict).execute();
        inOrder.verify(saveData).call();
        inOrder.verify(aboutDao).insert("2015-06-18", new HashMap<>());
        inOrder.verify(deleteDownloadedFiles).call();
//        inOrder.verify(exportByLang).call();
    }

}
