package com.frogdevelopment.nihongo.entries.api;

import com.frogdevelopment.nihongo.entries.implementation.populate.FetchEntries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping(path = "populate")
public class PopulateController {

    private final FetchEntries fetchEntries;

    @Autowired
    public PopulateController(FetchEntries fetchEntries) {
        this.fetchEntries = fetchEntries;
    }

    @GetMapping
    @ResponseStatus(ACCEPTED)
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @PreAuthorize("permitAll()")
    public void fetch() {
        fetchEntries.call();
    }

}
