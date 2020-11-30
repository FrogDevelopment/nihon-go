package com.frogdevelopment.authentication.application.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private boolean enabled;

    @NotNull
    @Size(min = 1)
    @Singular("authority")
    private Collection<String> authorities;

    @Singular("action")
    private Collection<String> actions;

}
