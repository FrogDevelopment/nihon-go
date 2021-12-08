package com.frogdevelopment.nihongo.lessons.application.lesson;

import com.frogdevelopment.nihongo.lessons.application.LessonService;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LessonServiceImpl implements LessonService {

    private final LessonDao lessonDao;

    @Override
    public List<InputDto> fetch(final int lesson, final String sortField, String sortOrder) {
        sortOrder = "descend".equalsIgnoreCase(sortOrder) ? "desc" : "asc";

        return lessonDao.fetch(lesson, sortField, sortOrder);
    }

}
