package com.frogdevelopment.nihongo.lessons;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.frogdevelopment.nihongo.export.ExportConfiguration;
import com.frogdevelopment.nihongo.ftp.FtpProperties;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.frogdevelopment")
@EnableTransactionManagement
@Import({ExportConfiguration.class})
@EnableConfigurationProperties({FtpProperties.class})
public class LessonsApplication {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(LessonsApplication.class).run(args);
    }
}


