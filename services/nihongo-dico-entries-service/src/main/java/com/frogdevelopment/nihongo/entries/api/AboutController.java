package com.frogdevelopment.nihongo.entries.api;

import lombok.RequiredArgsConstructor;

import com.frogdevelopment.nihongo.entries.implementation.about.AboutDao;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("about")
@RequiredArgsConstructor
public class AboutController {

    private final AboutDao aboutDao;

    @Get
//    @PreAuthorize("permitAll()")
    public String getAbout() {
        return aboutDao.getLast();
    }

    @Get("/languages")
//    @PreAuthorize("permitAll()")
    public String getLanguages() {
        return aboutDao.getLanguages();
    }
}
