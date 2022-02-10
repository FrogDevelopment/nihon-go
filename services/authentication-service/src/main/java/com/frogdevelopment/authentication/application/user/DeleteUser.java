package com.frogdevelopment.authentication.application.user;

import com.frogdevelopment.authentication.api.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

@Singleton
public class DeleteUser {

    private final JwtUserDetailsService jwtUserDetailsService;

    DeleteUser(JwtUserDetailsService jwtUserDetailsService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    public void call(String username) {
        if (!jwtUserDetailsService.userExists(username)) {
            throw new UserNotFoundException(username);
        }

        jwtUserDetailsService.deleteUser(username);
    }
}
