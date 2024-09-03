package com.akshay.book.authentication;

import com.akshay.book.email.EmailService;
import com.akshay.book.email.EmailTemplateName;
import com.akshay.book.response.Response;
import com.akshay.book.role.RoleRepository;
import com.akshay.book.user.Token;
import com.akshay.book.user.TokenRepository;
import com.akshay.book.user.User;
import com.akshay.book.user.UserRepository;
import jakarta.mail.MessagingException;
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
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    @Value("${account.activation.token.length}")
    private int activationTokenLength;
    @Value("${account.activation.token.expiry-time}")
    private int activationTokenExpiryTime;
    @Value("${account.activation-url}")
    private String activationUrl;

    public Response<?> registerUser(RegisterRequest request) throws  Exception{

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
       return Response.success("User registered successfully",null);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken  = generateAndSaveActivationToken(user);
        emailService.sendEmail(user.getEmail(),user.getFullName() , EmailTemplateName.ACTIVATE_ACCOUNT , activationUrl , newToken , "Account Activation");
    }

    private String generateAndSaveActivationToken(User user) {
//        generate token;
        String generatedToken = generateActivationToken(activationTokenLength);
        var token = Token.builder().
                token(generatedToken).
                createdAt(LocalDateTime.now()).
                expiresAt(LocalDateTime.now().plusMinutes(activationTokenExpiryTime)).
                user(user).build();

        tokenRepository.save(token);
        return generatedToken;
                
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
