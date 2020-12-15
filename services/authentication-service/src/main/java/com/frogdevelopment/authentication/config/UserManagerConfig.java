package com.frogdevelopment.authentication.config;

import com.frogdevelopment.jwt.JwtProcessTokenFilter;
import com.frogdevelopment.jwt.ResolveTokenToAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserManagerConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProcessTokenFilter jwtProcessTokenFilter(ResolveTokenToAuthentication tokenToAuthentication) {
        return new JwtProcessTokenFilter(tokenToAuthentication);
    }
}
