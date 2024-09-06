package com.akshay.book.authentication;

import com.akshay.book.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Log4j2
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

//    signup
    @PostMapping("/signup")
    public ResponseEntity<Response<?>> signUp(@RequestBody @Valid RegisterRequest request) {
        log.trace("Received registration request: {}", request);
        try {
            return ResponseEntity.ok().body(authenticationService.signUp(request));
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.error(e.getMessage()));
        }
    }

//    login

   @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> authenticateUser(@RequestBody @Valid LoginRequest request) {
        log.trace("Received login request: {}", request);
        try {
            return ResponseEntity.ok().body(authenticationService.loginUser(request));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.error(e.getMessage()));
        }

   }
}
