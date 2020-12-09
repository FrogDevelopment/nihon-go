package com.frogdevelopment.nihongo.entries.api;

import com.frogdevelopment.nihongo.entries.implementation.populate.FetchEntries;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping(path = "populate")
@RequiredArgsConstructor
public class PopulateController {

    private final FetchEntries fetchEntries;

    @PostMapping
    @ResponseStatus(ACCEPTED)
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @PreAuthorize("permitAll()")
    public void fetch() {
        fetchEntries.call();
    }

}
