package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Translation {

    int id;
    int japaneseId;
    String locale;
    String input;
    Character sortLetter;
    String details;
    String example;

    boolean toDelete;
}
