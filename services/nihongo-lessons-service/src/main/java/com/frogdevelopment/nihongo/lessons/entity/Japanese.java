package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Japanese {

    int id;
    @NonNull
    String kanji;
    String kana;

}
