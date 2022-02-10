package com.frogdevelopment.nihongo.secret;

import io.micronaut.context.env.MapPropertySource;
import io.micronaut.context.env.yaml.YamlPropertySourceLoader;
import io.micronaut.core.io.ResourceLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SecretPropertySourceLoader extends YamlPropertySourceLoader {

    private static final String SECRETS_PATH = "/run/secrets";

    @Override
    protected Optional<InputStream> readInput(final ResourceLoader resourceLoader, final String fileName) {
        final var path = Paths.get(SECRETS_PATH, fileName);
        if (!Files.exists(path)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Files.newInputStream(path));
        } catch (final IOException e) {
            log.error("Can't read file " + path, e);
            return Optional.empty();
        }
    }

    @Override
    protected MapPropertySource createPropertySource(final String name, final Map<String, Object> map, final int order) {
        return new MapPropertySource("docker-secrets:" + name, map) {
            @Override
            public int getOrder() {
                return order;
            }
        };
    }
}
