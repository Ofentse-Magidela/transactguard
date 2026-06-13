package com.transactguard.transactguard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDTO {

    @NotBlank
    @Size(min = 6, max = 26, message = "Username Must Be Between 6 - 26 Characters")
    private String username;

    @NotBlank
    @Size(min = 12, max = 26, message = "Password Must Be Between 12 - 26 Characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?])\\S*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;
}
