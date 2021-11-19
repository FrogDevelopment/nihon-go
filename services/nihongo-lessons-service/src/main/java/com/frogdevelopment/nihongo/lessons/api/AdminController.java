package com.frogdevelopment.nihongo.lessons.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frogdevelopment.nihongo.lessons.application.ExportLessons;
import com.frogdevelopment.nihongo.lessons.application.ManageLessons;
import com.frogdevelopment.nihongo.lessons.application.migrate.OldMigrateLessons;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin", produces = APPLICATION_JSON_VALUE)
public class AdminController {

    private final ManageLessons manageLessons;
    private final ExportLessons exportLessons;

    @PostMapping()
    public InputDto insert(@RequestBody final InputDto inputDto) {
        return manageLessons.insert(inputDto);
    }

    @PutMapping()
    public InputDto update(@RequestBody final InputDto inputDto) {
        return manageLessons.update(inputDto);
    }

    @DeleteMapping()
    public void delete(@RequestBody final InputDto inputDto) {
        manageLessons.delete(inputDto);
    }

    @PostMapping("/export")
    @PreAuthorize("permitAll()")
    public void export() {
        exportLessons.call();
    }

    // tmp
    private final OldMigrateLessons oldMigrateLessons;

    @PostMapping("/import_old")
    @PreAuthorize("permitAll()")
    public void importOld() {
        oldMigrateLessons.call();
    }
}

