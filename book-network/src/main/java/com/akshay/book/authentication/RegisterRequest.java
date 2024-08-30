package com.akshay.book.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class RegisterRequest {
    @NotBlank(message = "firstName can't be blank")
    private String firstName;

    @NotBlank(message = "lastName can't be blank")
    private String lastName;

    @NotNull(message = "email is required")
    @Email(message = "enter a valid email address")
    private String email;

    @NotNull(message = "password is required")
    @Size(min = 8 , message = "password should contain atleast 8 characters")
    private String password;
}
