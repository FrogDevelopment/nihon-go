package com.frogdevelopment.nihongo.sentences.api;

import com.frogdevelopment.nihongo.sentences.implementation.about.AboutDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "about", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AboutController {

    private final AboutDao aboutDao;

    @GetMapping("/languages")
    @PreAuthorize("permitAll()")
    public String getLanguages() {
        return aboutDao.getLanguages();
    }
}
