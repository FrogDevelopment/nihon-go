package com.frogdevelopment.nihongo.entries;

import io.micronaut.runtime.Micronaut;

public class EntriesApplication {

    public static void main(final String[] args) {
        Micronaut.build(args)
                .mainClass(EntriesApplication.class)
                .environments("postgres", "ftp")
                .banner(false)
                .start();
    }
}
