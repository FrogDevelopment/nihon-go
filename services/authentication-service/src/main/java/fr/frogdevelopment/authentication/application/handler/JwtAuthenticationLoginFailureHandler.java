package fr.frogdevelopment.authentication.application.handler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        log.error("AuthenticationFailure", exception);
        response.sendError(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase());
    }
}
