package fr.frogdevelopment.nihongo.sentence.api;

import static org.springframework.http.HttpStatus.ACCEPTED;

import fr.frogdevelopment.nihongo.sentence.implementation.populate.FetchSentences;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "populate")
public class PopulateController {

    private final FetchSentences fetchSentences;

    public PopulateController(FetchSentences fetchSentences) {
        this.fetchSentences = fetchSentences;
    }

    @GetMapping
    @ResponseStatus(ACCEPTED)
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @PreAuthorize("permitAll()")
    public void fetch() {
        fetchSentences.call();
    }

}
