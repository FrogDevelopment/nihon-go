package com.frogdevelopment.nihongo.lessons;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@Tag("integrationTest")
@MicronautTest
class LessonsApplicationTest {

    @Inject
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        Assertions.assertThat(applicationContext.isRunning()).isTrue();
    }

}
