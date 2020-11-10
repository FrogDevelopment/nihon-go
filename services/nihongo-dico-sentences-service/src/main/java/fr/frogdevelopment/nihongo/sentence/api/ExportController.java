package fr.frogdevelopment.nihongo.sentence.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import fr.frogdevelopment.nihongo.sentence.implementation.export.ExportDao;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "export", produces = APPLICATION_JSON_UTF8_VALUE)
public class ExportController {

    private final ExportDao exportDao;

    public ExportController(ExportDao exportDao) {
        this.exportDao = exportDao;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<String> exportForLanguage(@RequestParam String lang) {
        return exportDao.call(lang);
    }

}
