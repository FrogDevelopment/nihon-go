package com.frogdevelopment.nihongo.sentences.api;

import com.frogdevelopment.nihongo.sentences.implementation.search.Search;
import com.frogdevelopment.nihongo.sentences.implementation.search.entity.Sentence;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "search", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SearchController {

    private final Search search;

    @GetMapping
    @PreAuthorize("permitAll()")
    public Collection<Sentence> search(@RequestParam String lang,
                                       @RequestParam(required = false, value = "") String kanji,
                                       @RequestParam String kana,
                                       @RequestParam String gloss) {
        return search.call(lang, kanji, kana, gloss);
    }
}
