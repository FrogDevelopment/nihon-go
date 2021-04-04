package com.frogdevelopment.nihongo.lessons.implementation;

import com.frogdevelopment.nihongo.lessons.dao.LessonDao;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LessonService {

    private final LessonDao lessonDao;

    public int getTotal() {
        return lessonDao.getTotal();
    }

    public List<InputDto> fetch(final int pageIndex, final int pageSize, String sortField, String sortOrder) {
        if (StringUtils.isBlank(sortField)) {
            sortField = "japanese_id";
        }

        if (StringUtils.isNotBlank(sortOrder)) {
            sortOrder = "descend".equalsIgnoreCase(sortOrder) ? "desc" : "asc";
        } else {
            sortOrder = "asc";
        }

        return lessonDao.fetch(pageIndex, pageSize, sortField, sortOrder);
    }

    public List<String> getTags() {
        return lessonDao.getTags();
    }
}
