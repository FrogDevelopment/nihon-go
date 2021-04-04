package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Translation {

    int id;
    int japaneseId;
    @NonNull
    String lesson;
    @NonNull
    String locale;
    @NonNull
    String input;
    @NonNull
    String sortLetter;
    String details;
    String example;
    @Singular("tag")
    List<String> tags;

    boolean toDelete;
}
