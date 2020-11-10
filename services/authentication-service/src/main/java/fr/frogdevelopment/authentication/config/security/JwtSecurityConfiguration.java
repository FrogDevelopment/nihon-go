package fr.frogdevelopment.authentication.config.security;

import static org.springframework.http.HttpMethod.POST;

import com.frogdevelopment.jwt.JwtAuthorizationConfigurerAdapter;
import fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginFailureHandler;
import fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLoginSuccessHandler;
import fr.frogdevelopment.authentication.application.handler.JwtAuthenticationLogoutSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class JwtSecurityConfiguration extends JwtAuthorizationConfigurerAdapter {

    static final String LOGIN_ENTRY_POINT = "/login";
    static final String LOGOUT_ENTRY_POINT = "/logout";
    private static final String TOKEN_REFRESH_ENTRY_POINT = "/token/refresh";

    private final JwtAuthenticationLoginSuccessHandler authenticationSuccessHandler;
    private final JwtAuthenticationLoginFailureHandler authenticationFailureHandler;
    private final JwtAuthenticationLogoutSuccessHandler logoutSuccessHandler;

    public JwtSecurityConfiguration(JwtAuthenticationLoginSuccessHandler authenticationSuccessHandler,
                                    JwtAuthenticationLoginFailureHandler authenticationFailureHandler,
                                    JwtAuthenticationLogoutSuccessHandler logoutSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        configureLogin(http);
        configureLogout(http);
        configureEntryPoints(http);
    }

    protected final void configureLogin(HttpSecurity http) throws Exception {
        http.formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .loginProcessingUrl(LOGIN_ENTRY_POINT);
    }

    protected final void configureLogout(HttpSecurity http) throws Exception {
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_ENTRY_POINT, POST.name()))
                .logoutSuccessHandler(logoutSuccessHandler);
    }

    private void configureEntryPoints(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(LOGIN_ENTRY_POINT).permitAll()
                .antMatchers(LOGOUT_ENTRY_POINT).permitAll()
                .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll()
                .anyRequest().authenticated();
    }
}
