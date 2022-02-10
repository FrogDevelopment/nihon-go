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
public class Sense {

    @Singular("field")
    private List<String> field;
    @Singular("pos")
    private List<String> pos;
    @Singular("misc")
    private List<String> misc;
    private String info;
    @Singular("dial")
    private List<String> dial;
    @Singular("gloss")
    private List<Gloss> gloss;
}
