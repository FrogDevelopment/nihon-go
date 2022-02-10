package com.frogdevelopment.nihongo.lessons;

import io.micronaut.runtime.Micronaut;

public class LessonsApplication {

    public static void main(final String[] args) {
        Micronaut.build(args)
                .mainClass(LessonsApplication.class)
                .environments("postgres", "ftp")
                .banner(false)
                .start();
    }
}


