package fr.frogdevelopment.nihongo.lesson.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import fr.frogdevelopment.nihongo.lesson.dao.Input;
import fr.frogdevelopment.nihongo.lesson.entity.InputDto;
import fr.frogdevelopment.nihongo.lesson.implementation.LessonService;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class LessonController {

    private final LessonService lessonService;
    private final Environment environment;

    public LessonController(LessonService lessonService,
                            Environment environment) {
        this.lessonService = lessonService;
        this.environment = environment;
    }

    @GetMapping("/last_ready")
    public Integer getAvailableLessons(@RequestParam String locale) {
        return environment.getRequiredProperty("frog.lessons.last_ready." + locale, Integer.class);
    }

    @GetMapping(value = "/import")
    public List<Input> getLesson(@RequestParam String locale,
                                 @RequestParam String lesson) {
        return lessonService.getLesson(locale, lesson);
    }

    @GetMapping("/total")
    public int getTotal() {
        return lessonService.getTotal();
    }

    @GetMapping("/fetch")
    public List<InputDto> fetch(@RequestParam int pageIndex,
                                @RequestParam int pageSize,
                                @RequestParam(required = false) String sortField,
                                @RequestParam(required = false) String sortOrder) {
        return lessonService.fetch(pageIndex, pageSize, sortField, sortOrder);
    }

    @GetMapping("/tags")
    public List<String> getTags() {
        return lessonService.getTags();
    }

}
