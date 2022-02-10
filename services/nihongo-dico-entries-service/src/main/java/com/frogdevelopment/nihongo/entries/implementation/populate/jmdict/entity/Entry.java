package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

import io.micronaut.core.annotation.Introspected;

@Data
@Builder
@Introspected
@Jacksonized
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
