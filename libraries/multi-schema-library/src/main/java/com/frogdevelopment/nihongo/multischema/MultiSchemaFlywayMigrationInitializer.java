package com.frogdevelopment.nihongo.multischema;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;

@RequiredArgsConstructor
public class MultiSchemaFlywayMigrationInitializer implements InitializingBean, Ordered {

    private final FluentConfiguration configuration;

    private int order = 0;

    @Override
    public void afterPropertiesSet() {
        Arrays.stream(Language.values())
                .map(Language::getCode)
                .forEach(this::migrate);
    }

    private void migrate(String language) {
        this.configuration.schemas(language)
                .load()
                .migrate();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
