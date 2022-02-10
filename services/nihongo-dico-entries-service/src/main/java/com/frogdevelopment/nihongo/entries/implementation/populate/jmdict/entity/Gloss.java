package com.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import io.micronaut.core.annotation.Introspected;

@Data
@Builder
@Introspected
@Jacksonized
public class Gloss {

    private String lang;
    private String value;
}
