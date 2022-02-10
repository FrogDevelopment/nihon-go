package com.frogdevelopment.authentication.application.user;

import com.frogdevelopment.authentication.api.exception.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.frogdevelopment.authentication.application.user.UserTransformer.fromDto;
import static com.frogdevelopment.authentication.application.user.UserTransformer.toDto;

@Singleton
public class AddUser {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    AddUser(JwtUserDetailsService jwtUserDetailsService,
            PasswordEncoder passwordEncoder) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto call(UserDto userDto) {
        if (jwtUserDetailsService.userExists(userDto.getUsername())) {
            throw new UserAlreadyExistsException(userDto.getUsername());
        }

        var userDetails = User.withUserDetails(fromDto(userDto))
                .passwordEncoder(passwordEncoder::encode)
                .build();

        jwtUserDetailsService.createUser(userDetails);

        return toDto(userDetails);
    }

}
