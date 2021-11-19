package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Japanese {

    int id;
    String kanji;
    @NonNull
    String kana;

}
