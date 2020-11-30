package com.frogdevelopment.nihongo.entries.api;

import com.frogdevelopment.nihongo.entries.implementation.search.Search;
import com.frogdevelopment.nihongo.entries.implementation.search.SearchDao;
import com.frogdevelopment.nihongo.entries.implementation.search.entity.SearchDetails;
import com.frogdevelopment.nihongo.entries.implementation.search.entity.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SearchController {

    private final Search search;
    private final SearchDao searchDao;

    @GetMapping("{query}")
    @PreAuthorize("permitAll()")
    public Collection<SearchResult> search(@PathVariable final String query,
                                           @RequestParam final String lang) {
        return search.call(lang, query);
    }

    @GetMapping("/details/{senseSeq}")
    @PreAuthorize("permitAll()")
    public SearchDetails getDetails(@PathVariable final String senseSeq,
                                    @RequestParam final String lang) {
        return searchDao.getDetails(lang, senseSeq);
    }
}
