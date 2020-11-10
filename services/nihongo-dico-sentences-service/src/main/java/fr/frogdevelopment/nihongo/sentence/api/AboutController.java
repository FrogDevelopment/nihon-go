package fr.frogdevelopment.nihongo.sentence.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import fr.frogdevelopment.nihongo.sentence.implementation.about.AboutDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "about", produces = APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

    private final AboutDao aboutDao;

    public AboutController(AboutDao aboutDao) {
        this.aboutDao = aboutDao;
    }

    @GetMapping("/languages")
    @PreAuthorize("permitAll()")
    public String getLanguages() {
        return aboutDao.getLanguages();
    }
}
