package com.frogdevelopment.nihongo.lessons.application;

import java.util.List;

import com.frogdevelopment.nihongo.lessons.entity.InputDto;

public interface LessonService {
    int getTotal();

    List<InputDto> fetch(int pageIndex, int pageSize, String sortField, String sortOrder);

    List<String> getTags();
}
