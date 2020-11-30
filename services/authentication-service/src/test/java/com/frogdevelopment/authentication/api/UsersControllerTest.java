package com.frogdevelopment.authentication.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogdevelopment.authentication.application.user.AddUser;
import com.frogdevelopment.authentication.application.user.DeleteUser;
import com.frogdevelopment.authentication.application.user.GetUserDetails;
import com.frogdevelopment.authentication.application.user.ListUsers;
import com.frogdevelopment.authentication.application.user.UpdateUser;
import com.frogdevelopment.authentication.application.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SpringJUnitWebConfig
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integrationTest")
class UsersControllerTest {

    private static final String URL_TEMPLATE = "/users";
    private static final String USERNAME = "John";
    private static final UserDto USER_DTO = UserDto.builder()
            .username(USERNAME)
            .password("password")
            .authority("my_authority")
            .build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddUser addUser;
    @MockBean
    private UpdateUser updateUser;
    @MockBean
    private DeleteUser deleteUser;
    @MockBean
    private ListUsers listUsers;
    @MockBean
    private GetUserDetails getUserDetails;

    private JacksonTester<UserDto> jsonUserDto;

    @BeforeEach
    void setup() {
        var objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    @WithMockUser(roles = {"USER_ADD"})
    void addShouldReturnCreatedWhenROLE_USER_ADD() throws Exception {
        // given
        given(addUser.call(USER_DTO))
                .willReturn(USER_DTO);
        var content = givenUserContent();
        var requestBuilder = givenAddRequest(content);

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().json(content));
    }


    @Test
    @WithMockUser(roles = {"ADMIN_USERS"})
    void addShouldReturnCreatedWhenROLE_ADMIN_USERS() throws Exception {
        // given
        given(addUser.call(USER_DTO))
                .willReturn(USER_DTO);
        var content = givenUserContent();
        var requestBuilder = givenAddRequest(content);

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().json(content));
    }

    @Test
    @WithMockUser
    void addShouldReturnForbiddenWhenIncorrectRole() throws Exception {
        // given
        var content = givenUserContent();
        var requestBuilder = givenAddRequest(content);

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isForbidden());

        then(addUser).shouldHaveNoInteractions();
    }

    @Test
    @WithMockUser(roles = {"USER_UPDATE"})
    void updateShouldReturnAcceptedWhenROLE_USER_UPDATE() throws Exception {
        // given
        given(updateUser.call(USER_DTO))
                .willReturn(USER_DTO);
        var content = givenUserContent();
        var requestBuilder = givenUpdateRequest(content);

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isAccepted())
                .andExpect(content().json(content));
    }

    @Test
    @WithMockUser(roles = {"ADMIN_USERS"})
    void updateShouldReturnAcceptedWhenROLE_ADMIN_USERS() throws Exception {
        // given
        given(updateUser.call(USER_DTO))
                .willReturn(USER_DTO);
        var content = givenUserContent();
        var requestBuilder = givenUpdateRequest(content);

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isAccepted())
                .andExpect(content().json(content));
    }

    @Test
    @WithMockUser
    void updateShouldReturnForbiddenWhenIncorrectRole() throws Exception {
        // given
        var content = givenUserContent();
        var requestBuilder = givenUpdateRequest(content);

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isForbidden());

        then(updateUser).shouldHaveNoInteractions();
    }

    @Test
    @WithMockUser(roles = {"USER_DELETE"})
    void deleteShouldReturnAcceptedWhenROLE_USER_DELETE() throws Exception {
        // given
        var requestBuilder = givenDeleteRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isAccepted());

        then(deleteUser).should().call(USERNAME);
    }

    @Test
    @WithMockUser(roles = {"ADMIN_USERS"})
    void deleteShouldReturnAcceptedWhenROLE_ADMIN_USERS() throws Exception {
        // given
        var requestBuilder = givenDeleteRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isAccepted());

        then(deleteUser).should().call(USERNAME);
    }

    @Test
    @WithMockUser
    void deleteShouldReturnForbiddenWhenIncorrectRole() throws Exception {
        // given
        var requestBuilder = givenDeleteRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isForbidden());

        then(deleteUser).shouldHaveNoInteractions();
    }

    @Test
    @WithMockUser(roles = {"USERS_LIST"})
    void listShouldReturnOKWhenROLE_USERS_LIST() throws Exception {
        // given
        given(listUsers.call())
                .willReturn(List.of(USER_DTO));
        var requestBuilder = givenListRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN_USERS"})
    void listShouldReturnOKWhenROLE_ADMIN_USERS() throws Exception {
        // given
        given(listUsers.call())
                .willReturn(List.of(USER_DTO));
        var requestBuilder = givenListRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void listShouldReturnForbiddenWhenIncorrectRole() throws Exception {
        // given
        var requestBuilder = givenListRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isForbidden());

        then(listUsers).shouldHaveNoInteractions();
    }

    @Test
    @WithMockUser(USERNAME)
    void getShouldReturnOKWhenItself() throws Exception {
        // given
        var requestBuilder = givenGetRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER_UPDATE"})
    void getShouldReturnOkWhenNotItselfBut_ROLE_USER_UPDATE() throws Exception {
        // given
        var requestBuilder = givenGetRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN_USERS"})
    void getShouldReturnOkWhenROLE_ADMIN_USERS() throws Exception {
        // given
        var requestBuilder = givenGetRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getShouldReturnForbiddenWhenNotItself() throws Exception {
        // given
        var requestBuilder = givenGetRequest();

        // when
        var resultActions = whenPerform(requestBuilder);

        // then
        resultActions
                .andExpect(status().isForbidden());
    }

    private String givenUserContent() throws java.io.IOException {
        return jsonUserDto.write(USER_DTO).getJson();
    }

    private MockHttpServletRequestBuilder givenAddRequest(String content) {
        return post(URL_TEMPLATE)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(content)
                .with(SecurityMockMvcRequestPostProcessors.csrf());
    }

    private MockHttpServletRequestBuilder givenUpdateRequest(String content) {
        return put(URL_TEMPLATE)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(content)
                .with(SecurityMockMvcRequestPostProcessors.csrf());
    }

    private MockHttpServletRequestBuilder givenDeleteRequest() {
        return delete(URL_TEMPLATE)
                .param("username", USERNAME)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf());
    }

    private MockHttpServletRequestBuilder givenListRequest() {
        return get(URL_TEMPLATE)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf());
    }

    private MockHttpServletRequestBuilder givenGetRequest() {
        return get(URL_TEMPLATE + "/{username}", USERNAME)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf());
    }

    private ResultActions whenPerform(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }

}
