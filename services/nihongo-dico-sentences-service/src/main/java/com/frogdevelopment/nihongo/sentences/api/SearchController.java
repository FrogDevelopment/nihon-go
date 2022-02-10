package com.frogdevelopment.nihongo.sentences.api;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import com.frogdevelopment.nihongo.sentences.implementation.search.Search;
import com.frogdevelopment.nihongo.sentences.implementation.search.entity.Sentence;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

@Controller("search")
@RequiredArgsConstructor
public class SearchController {

    private final Search search;

    @Get
//    @PreAuthorize("permitAll()")
    public Collection<Sentence> search(@QueryValue final String lang,
            @QueryValue(defaultValue = "") final String kanji,
            @QueryValue final String kana,
            @QueryValue final String gloss) {
        return search.call(lang, kanji, kana, gloss);
    }
}
