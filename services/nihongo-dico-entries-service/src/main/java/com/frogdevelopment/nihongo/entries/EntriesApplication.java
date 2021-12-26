package com.frogdevelopment.nihongo.entries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.frogdevelopment.nihongo.export.ExportConfiguration;
import com.frogdevelopment.nihongo.ftp.FtpProperties;

@EnableAsync
@EnableScheduling
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.frogdevelopment")
@Import({ExportConfiguration.class})
@EnableConfigurationProperties({FtpProperties.class})
public class EntriesApplication {

    public static void main(final String[] args) {
        SpringApplication.run(EntriesApplication.class);
    }
}
