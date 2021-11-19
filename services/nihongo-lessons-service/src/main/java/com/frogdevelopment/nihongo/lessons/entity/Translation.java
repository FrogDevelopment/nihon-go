package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Translation {

    int id;
    int japaneseId;
    int lesson;
    String locale;
    String input;
    Character sortLetter;
    String details;
    String example;
    @Singular("tag")
    List<String> tags;

    boolean toDelete;
}
