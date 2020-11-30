package com.frogdevelopment.multischema.database;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
class SchemaContextHolderTest {

    @Test
    void shouldStoreSchemaToThread() {
        // when
        SchemaContextHolder.set("abc");

        // then
        assertThat(SchemaContextHolder.get()).isEqualTo("abc");

        // when
        SchemaContextHolder.clear();

        // then
        assertThat(SchemaContextHolder.get()).isNull();
    }

}