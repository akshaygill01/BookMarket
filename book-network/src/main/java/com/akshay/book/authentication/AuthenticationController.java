package com.akshay.book.authentication;

import com.akshay.book.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
@Log4j2
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public ResponseEntity<Response<String>> registerUser(@RequestBody @Valid RegisterRequest request) {
        try {
            return ResponseEntity.ok().body(authenticationService.registerUser(request));
        } catch (Exception e) {

        }
    }
}
