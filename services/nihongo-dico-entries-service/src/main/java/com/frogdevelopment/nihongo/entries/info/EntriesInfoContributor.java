package com.frogdevelopment.nihongo.entries.info;

import com.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntriesInfoContributor implements InfoContributor {

    private final AboutDao aboutDao;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("about", aboutDao.getLast());
    }
}
