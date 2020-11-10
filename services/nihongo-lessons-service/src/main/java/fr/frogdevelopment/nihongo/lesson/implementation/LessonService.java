package fr.frogdevelopment.nihongo.lesson.implementation;

import fr.frogdevelopment.nihongo.lesson.dao.Input;
import fr.frogdevelopment.nihongo.lesson.dao.LessonDao;
import fr.frogdevelopment.nihongo.lesson.entity.InputDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LessonService {

    private final LessonDao lessonDao;

    public LessonService(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    public List<Input> getLesson(String locale, String lesson) {
        return lessonDao.getLesson(locale, lesson);
    }

    public int getTotal() {
        return lessonDao.getTotal();
    }

    public List<InputDto> fetch(int pageIndex, int pageSize, String sortField, String sortOrder) {
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
