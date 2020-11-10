package fr.frogdevelopment.authentication.application.user;

import java.util.Collection;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

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
