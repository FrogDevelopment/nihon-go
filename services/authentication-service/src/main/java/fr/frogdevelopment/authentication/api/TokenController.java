package fr.frogdevelopment.authentication.api;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import fr.frogdevelopment.authentication.application.token.RefreshAccessToken;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = "/token",
        produces = TEXT_PLAIN_VALUE
)
public class TokenController {

    private final RefreshAccessToken refreshAccessToken;

    public TokenController(RefreshAccessToken refreshAccessToken) {
        this.refreshAccessToken = refreshAccessToken;
    }

    @ResponseStatus(OK)
    @GetMapping("/refresh")
    @PreAuthorize("isAuthenticated()")
    public String refreshToken(HttpServletRequest request,
                               @RequestParam(name = "time_zone") String timeZone) {
        return refreshAccessToken.call(timeZone, request);
    }
}
