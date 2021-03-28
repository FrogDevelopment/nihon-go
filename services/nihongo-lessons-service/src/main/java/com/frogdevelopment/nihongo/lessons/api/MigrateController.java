package com.frogdevelopment.nihongo.lessons.api;

import com.frogdevelopment.nihongo.lessons.implementation.migrate.OldMigrateLessons;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "migrate")
@RequiredArgsConstructor
public class MigrateController {

    private final OldMigrateLessons oldMigrateLessons;

    @PostMapping("/import_old")
    @PreAuthorize("permitAll()")
    public void importOld() {
        oldMigrateLessons.call();
    }
}
