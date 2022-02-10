package com.frogdevelopment.nihongo.sentences.api;

import lombok.RequiredArgsConstructor;

import com.frogdevelopment.nihongo.sentences.implementation.populate.FetchSentences;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;

import static io.micronaut.http.HttpStatus.ACCEPTED;

@Controller("populate")
@RequiredArgsConstructor
public class PopulateController {

    private final FetchSentences fetchSentences;

    @Post
    @Status(ACCEPTED)
//    @PreAuthorize("hasAnyRole('ADMIN')") fixme
//    @PreAuthorize("permitAll()")
    public void fetch() {
        fetchSentences.call();
    }

}
