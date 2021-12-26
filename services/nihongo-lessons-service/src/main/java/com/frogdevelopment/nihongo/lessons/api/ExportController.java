package com.frogdevelopment.nihongo.lessons.api;

import com.frogdevelopment.nihongo.lessons.application.ExportLessons;
import com.frogdevelopment.nihongo.lessons.application.migrate.OldMigrateLessons;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.entity.LessonInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/export")
public class ExportController {

    private final ExportLessons exportLessons;
    private final LessonDao lessonDao;

    @GetMapping("/{lesson}")
    @PreAuthorize("permitAll()")
    @ResponseStatus(code = OK)
    public Optional<LessonInfo> getLessonInfo(@PathVariable final int lesson) {
        return lessonDao.getLessonInfo(lesson);
    }

    @PostMapping("/{lesson}")
    @PreAuthorize("permitAll()")
    @ResponseStatus(code = NO_CONTENT)
    public void exportLesson(@PathVariable final int lesson) {
        exportLessons.call(lesson);
    }

    // tmp
    private final OldMigrateLessons oldMigrateLessons;

    @PostMapping("/import_old")
    @PreAuthorize("permitAll()")
    @ResponseStatus(code = NO_CONTENT)
    public void importOld() {
        oldMigrateLessons.call();
    }
}

