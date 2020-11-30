package com.frogdevelopment.authentication.application.token;

import com.frogdevelopment.jwt.ResolveClaimsFromToken;
import com.frogdevelopment.jwt.RetrieveTokenFromRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class JwtParserTest {

    private static final String ID = UUID.randomUUID().toString();

    @InjectMocks
    private JwtParser jwtParser;
    @Mock
    private ResolveClaimsFromToken resolveClaimsFromToken;
    @Mock
    private RetrieveTokenFromRequest retrieveTokenFromRequest;

    @Test
    void getIdFromRefreshToken_shouldReturn_null_when_incorrect_request() {
        // given
        var request = new MockHttpServletRequest();
        given(retrieveTokenFromRequest.call(request)).willReturn(null);

        // when
        var username = jwtParser.getIdFromRefreshToken(request);

        // then
        assertNull(username);
    }

    @Test
    void refreshToken_should_return_username() {
        // given
        var token = "my-token";
        Claims claims = new DefaultClaims();
        claims.setId(ID);
        given(resolveClaimsFromToken.call(token)).willReturn(claims);

        var request = new MockHttpServletRequest();
        given(retrieveTokenFromRequest.call(request)).willReturn(token);

        // when
        var id = jwtParser.getIdFromRefreshToken(request);

        // then
        assertEquals(ID, id);
    }

}
