package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.jdbc.annotation.ColumnTransformer;

@Value
@Builder(toBuilder = true)
@MappedEntity("japaneses")
public class Japanese {

    @Id
    @GeneratedValue
    @Nullable
    @MappedProperty("japanese_id")
    Long id;

    @ColumnTransformer(read = "TRIM(@.kanji)")
    String kanji;

    @NonNull
    @io.micronaut.core.annotation.NonNull
    @ColumnTransformer(read = "TRIM(@.kana)")
    String kana;

    Integer lesson;

}
