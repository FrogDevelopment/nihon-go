package com.frogdevelopment.nihongo.entries.implementation.export;

import com.frogdevelopment.nihongo.entries.api.ExportNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadAsResource {

    private final FileSystemResourceLoader fileSystemResourceLoader;

    public Resource call(String lang) {
        var location = ExportWriter.getLocation(lang);
        var resource = fileSystemResourceLoader.getResource("file:" + location);
        if (resource.exists()) {
            return resource;
        } else {
            throw new ExportNotFoundException(lang);
        }
    }

}
