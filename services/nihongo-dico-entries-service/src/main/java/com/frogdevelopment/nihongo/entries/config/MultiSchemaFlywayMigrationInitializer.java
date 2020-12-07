package com.frogdevelopment.nihongo.entries.config;

import com.frogdevelopment.nihongo.entries.implementation.Language;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;

import java.util.Arrays;

@RequiredArgsConstructor
public class MultiSchemaFlywayMigrationInitializer implements InitializingBean, Ordered {

    private final FluentConfiguration configuration;

    private int order = 0;

    @Override
    public void afterPropertiesSet() {
        Arrays.stream(Language.values())
                .map(Language::getCode)
                .forEach(language -> this.configuration.schemas(language).load().migrate());
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
