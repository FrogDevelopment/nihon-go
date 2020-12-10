package com.frogdevelopment.nihongo.sentences.implementation.about;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetAbout {

    private final AboutDao aboutDao;

    public GetAbout(AboutDao aboutDao) {
        this.aboutDao = aboutDao;
    }

    @Transactional
    public String call() {
        return aboutDao.getLast();
    }
}
