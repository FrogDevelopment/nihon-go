package com.frogdevelopment.nihongo.lessons.api;

import lombok.RequiredArgsConstructor;

import java.util.List;
import com.frogdevelopment.nihongo.lessons.application.LessonService;
import com.frogdevelopment.nihongo.lessons.application.ManageLessons;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;

import static io.micronaut.http.HttpStatus.CREATED;
import static io.micronaut.http.HttpStatus.NO_CONTENT;
import static io.micronaut.http.HttpStatus.OK;

@Controller("lessons")
@RequiredArgsConstructor
public class LessonController {

    private static final String DEFAULT_SORT_FIELD = "japanese_id";
    private static final String DEFAULT_SORT_ORDER = "asc";

    private final LessonService lessonService;
    private final ManageLessons manageLessons;

    @Get
    @Status(OK)
    public List<InputDto> fetch(@QueryValue final int lesson,
            @QueryValue(defaultValue = DEFAULT_SORT_FIELD) final String sortField,
            @QueryValue(defaultValue = DEFAULT_SORT_ORDER) final String sortOrder) {
        return lessonService.fetch(lesson, sortField, sortOrder);
    }

    @Post
    @Status(CREATED)
    public InputDto insert(@Body final InputDto inputDto) {
        return manageLessons.insert(inputDto);
    }

    @Put
    @Status(OK)
    public InputDto update(@Body final InputDto inputDto) {
        return manageLessons.update(inputDto);
    }

    @Delete
    @Status(NO_CONTENT)
    public void delete(@Body final InputDto inputDto) {
        manageLessons.delete(inputDto);
    }

}
