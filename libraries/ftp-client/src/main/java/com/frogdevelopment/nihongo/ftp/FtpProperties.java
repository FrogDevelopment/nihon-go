package com.frogdevelopment.nihongo.ftp;

import lombok.Data;

import io.micronaut.context.annotation.ConfigurationProperties;

@Data
@ConfigurationProperties("frog.ftp")
public class FtpProperties {

    private String server;
    private int port = 21;
    private String user;
    private String password;
    private String remotePath;
}
