package com.transactguard.transactguard.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

    @Size(min = 6, max = 26, message = "Username Must Be Between 6 - 26 Characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username must contain only alphabetic characters (no spaces or numbers)")
    private String username;

    @Pattern(regexp = "^\\S+$", message = "Email cannot contain spaces")
    @Email(message = "Invalid Email. Please Provide A Valid Email Format")
    @Size(min = 6, max = 256, message = "Email Must Be A Minimum Of 8 Characters And 256 Max")
    private String email;

    @Size(min = 12, max = 26, message = "Password Must Be Between 12 - 26 Characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]).\\S*$",
            message = "Password cannot contain spaces and must include uppercase, lowercase, a number, and a special character")
    private String password;
}
