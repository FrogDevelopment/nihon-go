package com.frogdevelopment.nihongo.entries.api;

import lombok.RequiredArgsConstructor;

import com.frogdevelopment.nihongo.entries.implementation.populate.FetchEntries;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;

import static io.micronaut.http.HttpStatus.ACCEPTED;

@Controller("populate")
@RequiredArgsConstructor
public class PopulateController {

    private final FetchEntries fetchEntries;

    @Post
    @Status(ACCEPTED)
//    @PreAuthorize("hasAnyRole('ADMIN')") fixme
//    @PreAuthorize("permitAll()")
    public void fetch() {
        fetchEntries.call();
    }

}
