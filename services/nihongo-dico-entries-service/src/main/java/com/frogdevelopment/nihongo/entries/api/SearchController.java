package com.frogdevelopment.nihongo.entries.api;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import com.frogdevelopment.nihongo.entries.implementation.search.Search;
import com.frogdevelopment.nihongo.entries.implementation.search.SearchDao;
import com.frogdevelopment.nihongo.entries.implementation.search.entity.SearchDetails;
import com.frogdevelopment.nihongo.entries.implementation.search.entity.SearchResult;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;

@Controller("search")
@RequiredArgsConstructor
public class SearchController {

    private final Search search;
    private final SearchDao searchDao;

    @Get("{query}")
//    @PreAuthorize("permitAll()")
    public Collection<SearchResult> search(@PathVariable final String query,
            @QueryValue final String lang) {
        return search.call(lang, query);
    }

    @Get("/details/{senseSeq}")
//    @PreAuthorize("permitAll()")
    public SearchDetails getDetails(@PathVariable final String senseSeq,
            @QueryValue final String lang) {
        return searchDao.getDetails(lang, senseSeq);
    }
}
