package com.frogdevelopment.nihongo.lessons.application;

import com.frogdevelopment.nihongo.lessons.entity.InputDto;

import java.util.List;

public interface LessonService {

    List<InputDto> fetch(int lesson, String sortField, String sortOrder);
}
