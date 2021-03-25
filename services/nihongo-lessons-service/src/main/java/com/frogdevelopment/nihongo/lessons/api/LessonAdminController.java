package com.frogdevelopment.nihongo.lessons.api;

import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.implementation.CreateLesson;
import com.frogdevelopment.nihongo.lessons.implementation.DeleteLesson;
import com.frogdevelopment.nihongo.lessons.implementation.UpdateLesson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin", produces = APPLICATION_JSON_VALUE)
public class LessonAdminController {

    private final CreateLesson createLesson;
    private final UpdateLesson updateLesson;
    private final DeleteLesson deleteLesson;

    @PostMapping()
    public InputDto insert(@RequestBody final InputDto inputDto) {
        return createLesson.call(inputDto);
    }

    @PutMapping()
    public InputDto update(@RequestBody final InputDto inputDto) {
        return updateLesson.call(inputDto);
    }

    @DeleteMapping()
    public void delete(@RequestBody final InputDto inputDto) {
        deleteLesson.call(inputDto);
    }
}

