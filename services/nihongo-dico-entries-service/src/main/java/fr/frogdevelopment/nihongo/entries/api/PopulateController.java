package fr.frogdevelopment.nihongo.entries.api;

import static org.springframework.http.HttpStatus.ACCEPTED;

import fr.frogdevelopment.nihongo.entries.implementation.populate.FetchEntries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "populate")
public class PopulateController {

    private final FetchEntries fetchEntries;

    @Autowired
    public PopulateController(FetchEntries fetchEntries) {
        this.fetchEntries = fetchEntries;
    }

    @GetMapping
    @ResponseStatus(ACCEPTED)
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @PreAuthorize("permitAll()")
    public void fetch() {
        fetchEntries.call();
    }

}
