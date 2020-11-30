package com.frogdevelopment.authentication.application.user;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integrationTest")
class ListUsersTest {

    @Autowired
    private ListUsers listUsers;

    @Test
    void shouldRetrieveListOfUsers() {
        // when
        var users = listUsers.call();

        // then
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("admin");
        assertThat(users.get(0).isEnabled()).isTrue();
        assertThat(users.get(0).getAuthorities()).containsOnly("ROLE_SUPER_ADMIN");
    }
}
