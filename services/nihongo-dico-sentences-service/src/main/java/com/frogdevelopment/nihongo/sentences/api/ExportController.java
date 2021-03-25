package com.frogdevelopment.nihongo.sentences.api;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frogdevelopment.nihongo.export.LoadExportAsResource;

@RestController
@RequestMapping(path = "export", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ExportController {

    private final LoadExportAsResource loadExportAsResource;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Resource> exportForLanguage(@RequestParam final String lang) {
        final Resource file = loadExportAsResource.call(lang);
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}
