package com.akshay.book.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class LoginRequest {
    @NotNull(message = "email is required")
    @Email(message = "enter a valid email address")
    private String email;

    @NotNull(message = "password is required")
    @Size(min = 8 , message = "password should contain atleast 8 characters")
    private String password;
}
