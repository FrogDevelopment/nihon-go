package com.frogdevelopment.authentication.application.handler;

import com.frogdevelopment.authentication.application.token.JwtParser;
import com.frogdevelopment.authentication.application.user.JwtUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationLogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler {

    private final JwtParser jwtParser;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtAuthenticationLogoutSuccessHandler(JwtParser jwtParser,
                                                 JwtUserDetailsService jwtUserDetailsService) {
        super();
        this.jwtParser = jwtParser;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        var jti = jwtParser.getIdFromRefreshToken(request);
        if (jti != null) {
            jwtUserDetailsService.addRevokedToken(jti);
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
