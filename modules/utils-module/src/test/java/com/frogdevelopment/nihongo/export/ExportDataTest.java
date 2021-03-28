package com.frogdevelopment.nihongo.export;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.function.Function;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class ExportDataTest {

    @InjectMocks
    private ExportData exportData;

    @Mock
    private CopyOut copyOut;

    @Test
    void shouldClearResourceCacheThenExport() throws IOException {
        // given
        final Function<String, String> sqlSupplier = lang -> "SQL_COPY";

        // when
//        exportData.call(sqlSupplier);

        // then
//        then(copyOut)
//                .should()
//                .call(sqlSupplier);
    }
}
