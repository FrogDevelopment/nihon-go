package com.frogdevelopment.nihongo.lessons.api;

import com.frogdevelopment.nihongo.lessons.application.LessonService;
import com.frogdevelopment.nihongo.lessons.application.ManageLessons;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/lessons")
public class LessonController {

    private static final String DEFAULT_SORT_FIELD = "japanese_id";
    private static final String DEFAULT_SORT_ORDER = "asc";

    private final LessonService lessonService;
    private final ManageLessons manageLessons;

    @ResponseStatus(code = OK)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<InputDto> fetch(@RequestParam final int lesson,
                                @RequestParam(defaultValue = DEFAULT_SORT_FIELD) final String sortField,
                                @RequestParam(defaultValue = DEFAULT_SORT_ORDER) final String sortOrder) {
        return lessonService.fetch(lesson, sortField, sortOrder);
    }

    @ResponseStatus(code = CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public InputDto insert(@RequestBody final InputDto inputDto) {
        return manageLessons.insert(inputDto);
    }

    @ResponseStatus(code = OK)
    @PutMapping(consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public InputDto update(@RequestBody final InputDto inputDto) {
        return manageLessons.update(inputDto);
    }

    @ResponseStatus(code = NO_CONTENT)
    @DeleteMapping(consumes = APPLICATION_JSON_VALUE)
    public void delete(@RequestBody final InputDto inputDto) {
        manageLessons.delete(inputDto);
    }

}
