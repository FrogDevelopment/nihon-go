package fr.frogdevelopment.nihongo.sentence.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import fr.frogdevelopment.nihongo.sentence.implementation.search.Search;
import fr.frogdevelopment.nihongo.sentence.implementation.search.entity.Sentence;
import java.util.Collection;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "search", produces = APPLICATION_JSON_UTF8_VALUE)
public class SearchController {

    private final Search search;

    public SearchController(Search search) {
        this.search = search;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public Collection<Sentence> search(@RequestParam String lang,
                                       @RequestParam(required = false, value = "") String kanji,
                                       @RequestParam String kana,
                                       @RequestParam String gloss) {
        return search.call(lang, kanji, kana, gloss);
    }
}
