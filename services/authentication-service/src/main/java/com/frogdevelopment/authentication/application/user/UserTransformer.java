package com.frogdevelopment.authentication.application.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTransformer {

    static User fromDto(UserDto userDto) {
        return new User(
                userDto.getUsername().toLowerCase(),
                userDto.getPassword(),
                userDto.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }

    static UserDto toDto(UserDetails user) {
        return UserDto.builder()
                .username(user.getUsername())
                .password(null)
                .authorities(
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet()))
                .build();
    }
}
