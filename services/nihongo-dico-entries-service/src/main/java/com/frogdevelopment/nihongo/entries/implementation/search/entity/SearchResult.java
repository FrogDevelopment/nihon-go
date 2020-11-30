package com.frogdevelopment.nihongo.entries.implementation.search.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class SearchResult {

    private String kanji;
    private String kana;
    private String reading;
    private String senseSeq;
    private String vocabulary;
    private double similarity;

}
