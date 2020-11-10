package fr.frogdevelopment.authentication.application.token;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    static final String AUTHORITIES_KEY = "authorities";

    private final String issuer; // fixme should be app url
    private final JwtProperties jwtProperties;

    @Autowired
    public TokenProvider(@Value("${spring.application.name}") String issuer,
                         JwtProperties jwtProperties) {
        this.issuer = issuer;
        this.jwtProperties = jwtProperties;
    }

    Token createAccessToken(UserDetails userDetails, String timeZone) {
        return createAccessToken(
                userDetails.getUsername(),
                timeZone,
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()));
    }

    public Token createAccessToken(Authentication authentication, String timeZone) {
        return createAccessToken(
                authentication.getName(),
                timeZone,
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );
    }

    private Token createAccessToken(String subject, String timeZone, Set<String> authorities) {
        if (isEmpty(authorities)) {
            throw new InsufficientAuthenticationException(format("User %s has no authorities assigned", subject));
        }

        return Token.builder()
                .issuer(issuer)
                .subject(subject)
                .claims(Map.of(AUTHORITIES_KEY, authorities))
                .expiration(jwtProperties.getAccessTokenExpiration())
                .chronoUnit(SECONDS)
                .timeZone(timeZone)
                .signingKey(jwtProperties.getSigningKey())
                .build();
    }

    public Token createRefreshToken(Authentication authentication, String timeZone) {
        return Token.builder()
                .id(UUID.randomUUID().toString())
                .issuer(issuer)
                .subject(authentication.getName())
                .expiration(jwtProperties.getRefreshTokenExpiration())
                .chronoUnit(HOURS)
                .timeZone(timeZone)
                .signingKey(jwtProperties.getSigningKey())
                .build();
    }

    public Token createIDToken(Authentication authentication, String clientID, String timeZone) {
        return Token.builder()
                .issuer(issuer)
                .audience(clientID)
                .subject(authentication.getName())
                .claims(Map.of(
                        "username", authentication.getName()
                ))
                .expiration(jwtProperties.getIDTokenExpiration())
                .chronoUnit(HOURS)
                .timeZone(timeZone)
                .signingKey(jwtProperties.getSigningKey())
                .build();
    }

}
