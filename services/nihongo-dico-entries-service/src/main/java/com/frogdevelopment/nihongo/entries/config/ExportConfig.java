package com.frogdevelopment.nihongo.entries.config;

import com.frogdevelopment.jwt.JwtProcessTokenFilter;
import com.frogdevelopment.jwt.ResolveTokenToAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResourceLoader;

@Configuration
public class ExportConfig {

    @Bean
    FileSystemResourceLoader fileSystemResourceLoader() {
        return new FileSystemResourceLoader();
    }

    @Bean
    public JwtProcessTokenFilter jwtProcessTokenFilter(ResolveTokenToAuthentication tokenToAuthentication){
        return new JwtProcessTokenFilter(tokenToAuthentication);
    }
}
