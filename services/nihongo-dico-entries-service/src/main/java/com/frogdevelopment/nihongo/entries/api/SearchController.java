package com.frogdevelopment.nihongo.entries.api;

import com.frogdevelopment.nihongo.entries.implementation.search.Search;
import com.frogdevelopment.nihongo.entries.implementation.search.SearchDao;
import com.frogdevelopment.nihongo.entries.implementation.search.entity.SearchDetails;
import com.frogdevelopment.nihongo.entries.implementation.search.entity.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

    private final Search search;
    private final SearchDao searchDao;

    @Autowired
    public SearchController(Search search,
                            SearchDao searchDao) {
        this.searchDao = searchDao;
        this.search = search;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public Collection<SearchResult> search(@RequestParam String lang,
                                           @RequestParam String query) {
        return search.call(lang, query);
    }

    @GetMapping("/details")
    @PreAuthorize("permitAll()")
    public SearchDetails getDetails(@RequestParam String lang,
                                    @RequestParam String senseSeq) {
        return searchDao.getDetails(lang, senseSeq);
    }
}
