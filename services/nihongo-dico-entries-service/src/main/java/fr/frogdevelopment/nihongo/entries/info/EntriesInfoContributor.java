package fr.frogdevelopment.nihongo.entries.info;

import fr.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class EntriesInfoContributor implements InfoContributor {

    private final AboutDao aboutDao;

    public EntriesInfoContributor(AboutDao aboutDao) {
        this.aboutDao = aboutDao;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("about", aboutDao.getLast());
    }
}
