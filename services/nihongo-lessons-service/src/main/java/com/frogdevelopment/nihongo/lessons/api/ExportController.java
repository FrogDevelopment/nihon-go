package com.frogdevelopment.nihongo.lessons.api;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import com.frogdevelopment.nihongo.lessons.application.ExportLessons;
import com.frogdevelopment.nihongo.lessons.application.migrate.OldMigrateLessons;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.entity.LessonInfo;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;

import static io.micronaut.http.HttpStatus.NO_CONTENT;
import static io.micronaut.http.HttpStatus.OK;

@Controller("export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportLessons exportLessons;
    private final LessonDao lessonDao;

    @Get("/{lesson}")
//    @PreAuthorize("permitAll()")
    @Status(OK)
    public Optional<LessonInfo> getLessonInfo(@PathVariable final int lesson) {
        return lessonDao.findById(lesson);
    }

    @Post("/{lesson}")
//    @PreAuthorize("permitAll()")
    @Status(NO_CONTENT)
    public void exportLesson(@PathVariable final int lesson) {
        exportLessons.call(lesson);
    }

    // tmp
    private final OldMigrateLessons oldMigrateLessons;

    @Post("/import_old")
//    @PreAuthorize("permitAll()")
    @Status(NO_CONTENT)
    public void importOld() {
        oldMigrateLessons.call();
    }
}

