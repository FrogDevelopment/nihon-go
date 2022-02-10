package com.frogdevelopment.nihongo.sentences;

import io.micronaut.runtime.Micronaut;

public class SentencesApplication {

    public static void main(final String[] args) {
        Micronaut.build(args)
                .mainClass(SentencesApplication.class)
                .environments("postgres", "ftp")
                .banner(false)
                .start();
    }
}
