package fr.frogdevelopment.nihongo.sentence.info;

import fr.frogdevelopment.nihongo.sentence.implementation.about.GetAbout;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class SentencesInfoContributor implements InfoContributor {

    private final GetAbout getAbout;

    public SentencesInfoContributor(GetAbout getAbout) {
        this.getAbout = getAbout;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("about", getAbout.call());
    }
}
