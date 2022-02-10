package com.frogdevelopment.nihongo.entries.implementation.search.entity;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.micronaut.core.annotation.Introspected;

@Data
@Builder
@Introspected
@JsonInclude(Include.NON_NULL)
public class SearchResult {

    private String kanji;
    private String kana;
    private String reading;
    private String senseSeq;
    private String vocabulary;
    private double similarity;

}
