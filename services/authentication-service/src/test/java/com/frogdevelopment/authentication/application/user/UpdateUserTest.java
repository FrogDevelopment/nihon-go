package com.frogdevelopment.authentication.application.user;

import com.frogdevelopment.authentication.api.exception.UserNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class UpdateUserTest {

    private static final String USERNAME = "username";

    @InjectMocks
    private UpdateUser updateUser;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDto user;

    @Test
    void add_should_throw_exception_when_user_doesnt_exist() {
        // given
        givenUser();
        givenUserAlreadyExists(false);

        // when
        var caughtThrowable = catchThrowable(() -> updateUser.call(user));

        // then
        assertThat(caughtThrowable)
                .isExactlyInstanceOf(UserNotFoundException.class)
                .hasMessage("Username [" + user.getUsername() + "] not found");

        then(jwtUserDetailsService).shouldHaveNoMoreInteractions();
        then(passwordEncoder).shouldHaveNoMoreInteractions();
    }

    @Test
    void add_should_create_user() {
        // given
        givenUser();
        givenUserAlreadyExists(true);
        given(passwordEncoder.encode(user.getPassword())).willReturn("encodedPassword");

        // when
        var createdUser = updateUser.call(user);

        // then
        var userArgumentCaptor = ArgumentCaptor.forClass(UserDetails.class);
        then(jwtUserDetailsService).should().updateUser(userArgumentCaptor.capture());
        var caughtUser = userArgumentCaptor.getValue();
        assertThat(caughtUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(caughtUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(caughtUser.getAuthorities())
                .extracting((Function<GrantedAuthority, Object>) GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrderElementsOf(user.getAuthorities());

        assertThat(createdUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(createdUser.getPassword()).isNull();
        assertThat(createdUser.getAuthorities()).containsExactlyInAnyOrderElementsOf(user.getAuthorities());

        then(jwtUserDetailsService).shouldHaveNoMoreInteractions();
        then(passwordEncoder).shouldHaveNoMoreInteractions();
    }

    private void givenUser() {
        user = UserDto.builder()
                .username(USERNAME)
                .password("password")
                .authority("USER")
                .authority("ADMIN")
                .build();
    }

    private void givenUserAlreadyExists(boolean exists) {
        given(jwtUserDetailsService.userExists(user.getUsername())).willReturn(exists);
    }

}
