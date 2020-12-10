package com.frogdevelopment.nihongo.sentences.api;

import com.frogdevelopment.nihongo.sentences.implementation.populate.FetchSentences;
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

    private final FetchSentences fetchSentences;

    @PostMapping
    @ResponseStatus(ACCEPTED)
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @PreAuthorize("permitAll()")
    public void fetch() {
        fetchSentences.call();
    }

}
