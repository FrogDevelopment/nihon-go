package com.frogdevelopment.nihongo.lessons.entity;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;

@MappedEntity("lessons")
public record LessonInfo(@Id Integer lesson,
                         @DateUpdated @MappedProperty("update_datetime") String updateDateTime,
                         @Nullable @MappedProperty("export_datetime") String exportDateTime) {
}
