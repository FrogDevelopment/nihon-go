package com.frogdevelopment.nihongo.sentences.info;

import com.frogdevelopment.nihongo.sentences.implementation.about.AboutDao;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SentencesInfoContributor implements InfoContributor {

    private final AboutDao aboutDao;

    @Override
    public void contribute(final Info.Builder builder) {
        builder.withDetail("about", aboutDao.getLast());
    }
}
