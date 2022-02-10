package com.frogdevelopment.nihongo.sentences;

import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integrationTest")
@MicronautTest
class SentencesApplicationTest {

    @Inject
    private EmbeddedServer embeddedServer;

    @Test
    void contextLoads() {
        Assertions.assertThat(embeddedServer.isRunning()).isTrue();
    }

}
