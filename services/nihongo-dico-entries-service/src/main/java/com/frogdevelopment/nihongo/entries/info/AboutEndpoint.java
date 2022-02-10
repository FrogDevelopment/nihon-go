package com.frogdevelopment.nihongo.entries.info;

import lombok.RequiredArgsConstructor;

import com.frogdevelopment.nihongo.entries.implementation.about.AboutDao;

import io.micronaut.management.endpoint.annotation.Endpoint;
import io.micronaut.management.endpoint.annotation.Read;

@Endpoint("about")
@RequiredArgsConstructor
public class AboutEndpoint {

    private final AboutDao aboutDao;

    @Read
    public String read() {
        return aboutDao.getLast();
    }
}
