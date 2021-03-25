package com.frogdevelopment.nihongo.lessons;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.frogdevelopment")
@EnableTransactionManagement
public class Application {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(Application.class).run(args);
    }
}


