package fr.frogdevelopment.authentication.application.user;

import static fr.frogdevelopment.authentication.application.user.UserTransformer.fromDto;
import static fr.frogdevelopment.authentication.application.user.UserTransformer.toDto;

import fr.frogdevelopment.authentication.api.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UpdateUser {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    UpdateUser(JwtUserDetailsService jwtUserDetailsService,
               PasswordEncoder passwordEncoder) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto call(UserDto userDto) {
        if (!jwtUserDetailsService.userExists(userDto.getUsername())) {
            throw new UserNotFoundException(userDto.getUsername());
        }

        var userDetails = User.withUserDetails(fromDto(userDto))
                .passwordEncoder(passwordEncoder::encode)
                .build();

        jwtUserDetailsService.updateUser(userDetails);

        return toDto(userDetails);
    }

}
