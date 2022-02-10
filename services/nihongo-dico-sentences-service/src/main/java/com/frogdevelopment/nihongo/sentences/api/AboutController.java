package com.frogdevelopment.nihongo.sentences.api;

import lombok.RequiredArgsConstructor;

import com.frogdevelopment.nihongo.sentences.implementation.about.AboutDao;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("about")
@RequiredArgsConstructor
public class AboutController {

    private final AboutDao aboutDao;

    @Get("/languages")
//    @PreAuthorize("permitAll()")
    public String getLanguages() {
        return aboutDao.getLanguages();
    }
}
