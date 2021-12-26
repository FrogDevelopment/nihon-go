package com.frogdevelopment.nihongo.ftp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("frog.ftp")
public class FtpProperties {

    private String server;
    private int port;
    private String user;
    private String password;
    private String remotePath;
}
