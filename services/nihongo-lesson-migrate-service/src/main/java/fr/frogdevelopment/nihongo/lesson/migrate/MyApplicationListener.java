package fr.frogdevelopment.nihongo.lesson.migrate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationContext ctx;
    private final MigrateLessons migrateLessons;

    public MyApplicationListener(ApplicationContext ctx, MigrateLessons migrateLessons) {
        this.ctx = ctx;
        this.migrateLessons = migrateLessons;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        migrateLessons.call();
        SpringApplication.exit(ctx, () -> 0);
    }
}
