package com.frogdevelopment.nihongo.lessons.api;

import com.frogdevelopment.nihongo.lessons.dao.Input;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.implementation.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class LessonController {

    private final LessonService lessonService;
    private final Environment environment;

    @GetMapping("/last_ready")
    public Integer getAvailableLessons(@RequestParam final String locale) {
        return environment.getRequiredProperty("frog.lessons.last_ready." + locale, Integer.class);
    }

    @GetMapping(value = "/import")
    public List<Input> getLesson(@RequestParam final String locale,
                                 @RequestParam final String lesson) {
        return lessonService.getLesson(locale, lesson);
    }

    @GetMapping("/total")
    public int getTotal() {
        return lessonService.getTotal();
    }

    @GetMapping("/fetch")
    public List<InputDto> fetch(@RequestParam final int pageIndex,
                                @RequestParam final int pageSize,
                                @RequestParam(required = false) final String sortField,
                                @RequestParam(required = false) final String sortOrder) {
        return lessonService.fetch(pageIndex, pageSize, sortField, sortOrder);
    }

    @GetMapping("/tags")
    public List<String> getTags() {
        return lessonService.getTags();
    }

}
