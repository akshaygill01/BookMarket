package com.akshay.book.authentication;

import com.akshay.book.response.Response;
import com.akshay.book.role.RoleRepository;
import com.akshay.book.user.Token;
import com.akshay.book.user.User;
import com.akshay.book.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("{accountactivationtoken.length}")
    private int activationTokenLength;
    private int activationTokenExpiryTime;

    public Response<String> registerUser(RegisterRequest request) throws  Exception{

       var userRole  =  roleRepository.findByName("USER").orElseThrow(()-> new IllegalArgumentException("role is not initialized"));

       var user  = User.builder().
                   firstName(request.getFirstName()).
                   lastName(request.getLastName()).
                   email(request.getEmail()).
                   password(passwordEncoder.encode(request.getPassword())).
                   accountLocked(false).
                   enabled(false).
                   roles(List.of(userRole)).
                   build();

       userRepository.save(user);
       sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) {
        var newToken  = generateAndSaveActivationToken(user);
    }

    private String generateAndSaveActivationToken(User user) {
//        generate token;
        String generatedToken = generateActivationToken(activationTokenLength);
//        send Email
        Token.builder().token(generatedToken).createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now() + activationTokenExpiryTime).build();
                
    }

    private String generateActivationToken(int length) {
        String validCharacters = "1234567890";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0;i<length;i++) {
            int randomIndex = random.nextInt(validCharacters.length());
            sb.append(validCharacters.charAt(randomIndex));
        }
        return sb.toString();
    }
}
