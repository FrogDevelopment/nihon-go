package com.frogdevelopment.nihongo.lessons.dao;

import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.LessonInfo;

import java.util.List;
import java.util.Optional;

public interface LessonDao {

    List<InputDto> fetch(int lesson, String sortField, String sortOrder);

    void upsertLesson(int lesson);

    void exportLesson(int lesson);

    void deleteLesson(int lesson);

    Optional<LessonInfo> getLessonInfo(int lesson);
}
