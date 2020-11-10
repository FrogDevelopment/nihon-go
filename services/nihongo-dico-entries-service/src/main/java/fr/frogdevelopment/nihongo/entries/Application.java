package fr.frogdevelopment.nihongo.entries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "fr.frogdevelopment")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
