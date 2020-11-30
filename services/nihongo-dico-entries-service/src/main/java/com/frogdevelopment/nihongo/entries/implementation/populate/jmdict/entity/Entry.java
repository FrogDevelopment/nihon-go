package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class Entry {

    private String seq;
    @Singular("kanji")
    private List<String> kanjis;
    private String kana;
    @Singular("sense")
    private List<Sense> senses;

    public String getKanji() {
        return kanjis.stream().findFirst().orElse("");
    }
}
