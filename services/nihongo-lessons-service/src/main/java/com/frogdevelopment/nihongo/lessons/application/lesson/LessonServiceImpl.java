package com.frogdevelopment.nihongo.lessons.application.lesson;

import static org.apache.commons.lang3.StringUtils.isBlank;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.frogdevelopment.nihongo.lessons.application.LessonService;
import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LessonServiceImpl implements LessonService {

    private final LessonDao lessonDao;

    @Override
    public int getTotal() {
        return lessonDao.getTotal();
    }

    @Override
    public List<InputDto> fetch(final int pageIndex, final int pageSize, String sortField, String sortOrder) {
        if (isBlank(sortField)) {
            sortField = "japanese_id";
        }

        if (isBlank(sortOrder)) {
            sortOrder = "asc";
        } else {
            sortOrder = "descend".equalsIgnoreCase(sortOrder) ? "desc" : "asc";
        }

        return lessonDao.fetch(pageIndex, pageSize, sortField, sortOrder);
    }

}
