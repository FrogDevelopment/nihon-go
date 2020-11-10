package fr.frogdevelopment.authentication.application.user;

import fr.frogdevelopment.authentication.api.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

@Component
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
