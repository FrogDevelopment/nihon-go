package com.frogdevelopment.nihongo.lessons.application.lesson;

import lombok.RequiredArgsConstructor;

import java.util.List;
import javax.transaction.Transactional;
import com.frogdevelopment.nihongo.lessons.application.LessonService;
import com.frogdevelopment.nihongo.lessons.dao.InputDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

import jakarta.inject.Singleton;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@Singleton
@RequiredArgsConstructor
@Transactional(REQUIRES_NEW)
public class LessonServiceImpl implements LessonService {

    private final InputDao inputDao;

    @Override
    public List<InputDto> fetch(final int lesson, final String sortField, String sortOrder) {
        sortOrder = "descend".equalsIgnoreCase(sortOrder) ? "desc" : "asc";

        return inputDao.fetch(lesson, sortField, sortOrder);
    }

}
