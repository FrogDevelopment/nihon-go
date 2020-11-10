package fr.frogdevelopment.nihongo.entries.implementation.export;

import fr.frogdevelopment.nihongo.entries.api.ExportNotFoundException;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class LoadAsResource {

    private final FileSystemResourceLoader fileSystemResourceLoader;

    public LoadAsResource(FileSystemResourceLoader fileSystemResourceLoader) {
        this.fileSystemResourceLoader = fileSystemResourceLoader;
    }

    public Resource call(String lang) {
        var location = String.format(ExportWriter.EXPORT_FORMAT, lang);
        var resource = fileSystemResourceLoader.getResource("file:" + location);
        if (resource.exists()) {
            return resource;
        } else {
            throw new ExportNotFoundException(lang);
        }
    }

}
