package com.frogdevelopment.authentication.application.user;

import com.frogdevelopment.authentication.api.exception.UserNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class DeleteUserTest {

    private static final String USERNAME = "username";

    @InjectMocks
    private DeleteUser deleteUser;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    void add_should_throw_exception_when_user_doesnt_exist() {
        // given
        givenUserAlreadyExists(false);

        // when
        var caughtThrowable = catchThrowable(() -> deleteUser.call(USERNAME));

        // then
        assertThat(caughtThrowable)
                .isExactlyInstanceOf(UserNotFoundException.class)
                .hasMessage("Username [" + USERNAME + "] not found");

        then(jwtUserDetailsService).shouldHaveNoMoreInteractions();
    }

    @Test
    void delete_should_delete_user() {
        // given
        givenUserAlreadyExists(true);

        // when
        deleteUser.call(USERNAME);

        // then
        then(jwtUserDetailsService).should().deleteUser(USERNAME);
    }

    private void givenUserAlreadyExists(boolean exists) {
        given(jwtUserDetailsService.userExists(USERNAME)).willReturn(exists);
    }

}
