package fr.frogdevelopment.nihongo.lesson.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import fr.frogdevelopment.nihongo.lesson.entity.InputDto;
import fr.frogdevelopment.nihongo.lesson.implementation.CreateLesson;
import fr.frogdevelopment.nihongo.lesson.implementation.DeleteLesson;
import fr.frogdevelopment.nihongo.lesson.implementation.UpdateLesson;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin", produces = APPLICATION_JSON_UTF8_VALUE)
public class LessonAdminController {

    private final CreateLesson createLesson;
    private final UpdateLesson updateLesson;
    private final DeleteLesson deleteLesson;

    public LessonAdminController(CreateLesson createLesson,
                                 UpdateLesson updateLesson,
                                 DeleteLesson deleteLesson) {
        this.createLesson = createLesson;
        this.updateLesson = updateLesson;
        this.deleteLesson = deleteLesson;
    }

    @PostMapping()
    public InputDto insert(@RequestBody InputDto inputDto) {
        return createLesson.call(inputDto);
    }

    @PutMapping()
    public InputDto update(@RequestBody InputDto inputDto) {
        return updateLesson.call(inputDto);
    }

    @DeleteMapping()
    public void delete(@RequestBody InputDto inputDto) {
        deleteLesson.call(inputDto);
    }
}

