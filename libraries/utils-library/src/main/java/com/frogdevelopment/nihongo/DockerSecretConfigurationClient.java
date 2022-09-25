package com.frogdevelopment.nihongo;

import io.micronaut.context.annotation.BootstrapContextCompatible;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.env.MapPropertySource;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.discovery.config.ConfigurationClient;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Context
@Requires(env = "docker")
@BootstrapContextCompatible
public class DockerSecretConfigurationClient implements ConfigurationClient {

    @Override
    @NonNull
    public String getDescription() {
        return "docker-secrets";
    }

    @Override
    public Publisher<PropertySource> getPropertySources(Environment environment) {
        var pathValue = environment.getProperty("docker-secrets.path", String.class, "/run/secrets");
        //noinspection ConstantConditions
        var path = Paths.get(pathValue);

        if (!Files.exists(path)) {
            log.warn("Docker Secrets directory [{}] doesn't exist. Skipping loading.", pathValue);
            return Mono.empty();
        }

        if (!Files.isDirectory(path)) {
            log.error("Docker Secrets directory [{}] is not a directory! Skipping loading.", pathValue);
            return Mono.empty();
        }

        log.info("Reading secrets at [{}]", path);

        //noinspection BlockingMethodInNonBlockingContext
        try (var files = Files.list(path)) {
            var secrets = files.collect(toMap());

            return Mono.just(new MapPropertySource("docker-secrets", secrets));
        } catch (IOException e) {
            log.error("Error while listing Docker Secrets in [{}]", pathValue, e);
            return Mono.empty();
        }

    }

    private Collector<Path, ?, Map<String, String>> toMap() {
        return Collectors.toMap(
                file -> "docker-secrets." + file.getFileName().toString(),
                this::readingSecret
        );
    }

    private String readingSecret(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            log.error("Unreadable Docker Secret [{}], replacing it with empty string", path.getFileName().toString(), e);
            return "";
        }
    }
}
