package com.frogdevelopment.authentication.application.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class JwtUserDetailsService extends JdbcUserDetailsManager {

    private static final String REVOKE_TOKEN = "INSERT INTO revoked_token(jti, revocation_datetime) VALUES (?, timenow())";
    private static final String IS_REVOKED = "SELECT EXISTS(SELECT jti FROM revoked_token WHERE jti = ?)";

    public JwtUserDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    public void addRevokedToken(String jti) {
        getJdbcTemplate().update(REVOKE_TOKEN, jti);
    }

    public boolean isRevoked(String jti) {
        return getJdbcTemplate().queryForObject(IS_REVOKED, Boolean.class, jti);
    }

    public void updateUser(final UserDetails user) {
        super.updateUser(user);


    }
}
