package fr.frogdevelopment.nihongo.entries.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExportNotFoundException extends RuntimeException {

    public ExportNotFoundException(String lang) {
        super(String.format("Export file not found for lang %s", lang));
    }
}
