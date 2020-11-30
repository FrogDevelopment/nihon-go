package com.frogdevelopment.authentication.api;

import com.frogdevelopment.authentication.application.user.AddUser;
import com.frogdevelopment.authentication.application.user.DeleteUser;
import com.frogdevelopment.authentication.application.user.GetUserDetails;
import com.frogdevelopment.authentication.application.user.ListUsers;
import com.frogdevelopment.authentication.application.user.UpdateUser;
import com.frogdevelopment.authentication.application.user.UserDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(
        path = "/users",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
)
public class UsersController {

    private final AddUser addUser;
    private final UpdateUser updateUser;
    private final DeleteUser deleteUser;
    private final ListUsers listUsers;
    private final GetUserDetails getUserDetails;

    public UsersController(AddUser addUser,
                           UpdateUser updateUser,
                           DeleteUser deleteUser,
                           ListUsers listUsers,
                           GetUserDetails getUserDetails) {
        this.addUser = addUser;
        this.updateUser = updateUser;
        this.deleteUser = deleteUser;
        this.listUsers = listUsers;
        this.getUserDetails = getUserDetails;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasRole('USER_ADD')")
    public UserDto add(@RequestBody @Valid UserDto userDto) {
        return addUser.call(userDto);
    }

    @PutMapping
    @ResponseStatus(ACCEPTED)
    @PreAuthorize("hasAnyRole('USER_UPDATE')")
    public UserDto update(@RequestBody @Valid UserDto userDto) {
        return updateUser.call(userDto);
    }

    @DeleteMapping
    @ResponseStatus(ACCEPTED)
    @PreAuthorize("hasRole('USER_DELETE')")
    public void delete(@PathParam("username") String username) {
        deleteUser.call(username);
    }

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasRole('USERS_LIST')")
    public List<UserDto> list() {
        return listUsers.call();
    }

    @GetMapping("/{username}")
    @ResponseStatus(OK)
    @PreAuthorize("isItself(#username) || hasRole('USER_UPDATE')")
    public UserDto get(@PathVariable String username) {
        return getUserDetails.call(username);
    }
}
