package com.frogdevelopment.nihongo.export;

import java.nio.file.Path;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@Tag("unitTest")
class PathExportManagerTest {

    @Test
    void should_returnThePathForLang(@TempDir Path tempDir) {
        // given
        final String path = tempDir.toString();
        var pathExportManager = new PathExportManager(path);

        // when
        final Path pathForLang = pathExportManager.getPathForLang("ENG");

        // then
        Assertions.assertThat(pathForLang.toString()).isEqualTo(path + "/ENG.json");
    }
}
