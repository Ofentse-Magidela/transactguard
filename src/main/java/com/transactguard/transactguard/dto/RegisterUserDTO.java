package com.transactguard.transactguard.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 6, max = 26, message = "Username Must Be Between 6 - 26 Characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid Email. Please Provide A Valid Email Format")
    @Size(min = 6, max = 256, message = "Email Must Be A Minimum Of 8 Characters And 256 Max")
    private String email;

    @NotNull(message = "Amount cannot be empty.")
    @DecimalMin(value = "0.00", message = "Minimum amount is R0.00")
    private Double balance;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 12, max = 26, message = "Password Must Be Between 12 - 26 Characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?])\\S*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;
}
