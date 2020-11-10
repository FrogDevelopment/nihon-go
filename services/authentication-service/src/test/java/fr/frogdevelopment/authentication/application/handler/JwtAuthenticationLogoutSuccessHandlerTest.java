package fr.frogdevelopment.authentication.application.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import fr.frogdevelopment.authentication.application.token.JwtParser;
import fr.frogdevelopment.authentication.application.user.JwtUserDetailsService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationLogoutSuccessHandlerTest {

    @InjectMocks
    private JwtAuthenticationLogoutSuccessHandler jwtAuthenticationLogoutSuccessHandler;

    @Mock
    private JwtParser jwtParser;
    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private Authentication authentication;
    @Mock
    private PrintWriter writer;

    @BeforeEach
    void beforeEach() throws IOException {
        given(httpServletResponse.getWriter()).willReturn(writer);
    }

    @Test
    void shouldAddRevokedToken() throws IOException {
        // given
        given(jwtParser.getIdFromRefreshToken(any())).willReturn("JTI");

        // when
        jwtAuthenticationLogoutSuccessHandler.onLogoutSuccess(httpServletRequest, httpServletResponse, authentication);

        // then
        verify(jwtUserDetailsService).addRevokedToken("JTI");
    }

    @Test
    void shouldNotAddRevokedToken() throws IOException {
        // given
        given(jwtParser.getIdFromRefreshToken(any())).willReturn(null);

        // when
        jwtAuthenticationLogoutSuccessHandler.onLogoutSuccess(httpServletRequest, httpServletResponse, authentication);

        // then
        verifyNoMoreInteractions(jwtUserDetailsService);
    }
}
