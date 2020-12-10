package com.frogdevelopment.nihongo.sentences.api;

import com.frogdevelopment.nihongo.sentences.implementation.export.ExportDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "export", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ExportController {

    private final ExportDao exportDao;

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<String> exportForLanguage(@RequestParam String lang) {
        return exportDao.call(lang);
    }

}
