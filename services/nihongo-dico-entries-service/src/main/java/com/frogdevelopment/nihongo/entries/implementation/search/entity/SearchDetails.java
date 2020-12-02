package com.frogdevelopment.nihongo.entries.implementation.search.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class SearchDetails {

    private long entrySeq;
    private String kanji;
    private String kana;
    private String reading;
    private Set<String> pos;
    private Set<String> field;
    private Set<String> misc;
    private String info;
    private Set<String> dial;
    private String gloss;
}
