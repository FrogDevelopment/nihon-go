package com.frogdevelopment.nihongo.lessons.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frogdevelopment.nihongo.lessons.application.LessonService;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/lessons", produces = APPLICATION_JSON_VALUE)
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public List<InputDto> fetch(@RequestParam final int pageIndex,
                                @RequestParam final int pageSize,
                                @RequestParam(required = false) final String sortField,
                                @RequestParam(required = false) final String sortOrder) {
        return lessonService.fetch(pageIndex, pageSize, sortField, sortOrder);
    }

    @GetMapping("/total")
    public int getTotal() {
        return lessonService.getTotal();
    }

    @GetMapping("/tags")
    public List<String> getTags() {
        return lessonService.getTags();
    }

}
