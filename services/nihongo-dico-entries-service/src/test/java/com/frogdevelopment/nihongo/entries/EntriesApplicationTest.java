package com.frogdevelopment.nihongo.entries;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@Tag("integrationTest")
@MicronautTest
class EntriesApplicationTest {

    @Inject
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        Assertions.assertThat(applicationContext.isRunning()).isTrue();
    }

}
