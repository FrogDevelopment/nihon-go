package com.frogdevelopment.nihongo.lessons.migrate.api;

import com.frogdevelopment.nihongo.lessons.migrate.implementation.MigrateLessons;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "migrate")
@RequiredArgsConstructor
public class MigrateController {

    private final MigrateLessons migrateLessons;

    @PostMapping
    @PreAuthorize("permitAll()")
    public void migrate() {
        migrateLessons.call();
    }
}
