package com.frogdevelopment.nihongo.lessons.api;

import com.frogdevelopment.nihongo.lessons.implementation.export.ExportLessons;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportLessons exportLessons;

    @PostMapping
    @PreAuthorize("permitAll()")
    public void importOld() {
        exportLessons.call();
    }
}
