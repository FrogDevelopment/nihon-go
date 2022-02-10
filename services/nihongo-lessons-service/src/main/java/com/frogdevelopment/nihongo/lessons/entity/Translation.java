package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.Data;

import com.frogdevelopment.nihongo.lessons.Utils;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.annotation.event.PrePersist;
import io.micronaut.data.jdbc.annotation.ColumnTransformer;

@Data
@Builder(toBuilder = true)
@MappedEntity("translations")
public class Translation {

    @Id
    @GeneratedValue
    @Nullable
    @MappedProperty("translation_id")
    final Long id;

    Long japaneseId;

    @NonNull
    final String locale;

    @NonNull
    @ColumnTransformer(write = "TRIM(?)")
    final String input;

    @NonNull
    String sortLetter;

    @ColumnTransformer(write = "TRIM(?)")
    final String details;

    @ColumnTransformer(write = "TRIM(?)")
    final String example;

//    @Transient
//    final boolean toDelete;

    @PrePersist
    void prepareData() {
        this.sortLetter = Utils.getSortLetter(this.input);
    }

}
