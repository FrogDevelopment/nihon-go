package com.frogdevelopment.nihongo.entries.api;

import com.frogdevelopment.nihongo.entries.implementation.export.LoadAsResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping(path = "export")
public class ExportController {

    private final LoadAsResource loadAsResource;

    public ExportController(LoadAsResource loadAsResource) {
        this.loadAsResource = loadAsResource;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Resource> exportForLanguage(@RequestParam String lang) {
        //write all users to csv file
        Resource file = loadAsResource.call(lang);
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}
