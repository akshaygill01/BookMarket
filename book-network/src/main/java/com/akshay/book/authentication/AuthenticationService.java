package com.akshay.book.authentication;

import com.akshay.book.email.EmailService;
import com.akshay.book.email.EmailTemplateName;
import com.akshay.book.models.LoginRequest;
import com.akshay.book.models.LoginResponse;
import com.akshay.book.models.Response;
import com.akshay.book.repository.RoleRepository;
import com.akshay.book.security.JwtService;
import com.akshay.book.bean.Token;
import com.akshay.book.repository.TokenRepository;
import com.akshay.book.bean.User;
import com.akshay.book.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Log4j2
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${account.activation.token.length}")
    private int activationTokenLength;
    @Value("${account.activation.token.expiry-time}")
    private int activationTokenExpiryTime;
    @Value("${account.activation-url}")
    private String activationUrl;

//    @Transactional(rollbackFor = Exception.class)
    public Response<?> signUp(RegisterRequest request) throws  Exception{

       var userRole  =  roleRepository.findByName("USER").orElseThrow(()-> new IllegalArgumentException("role is not initialized"));

       var isUserAlreadyExists  = userRepository.getUserByEmail(request.getEmail());
       if(isUserAlreadyExists.isPresent()) {
           throw new IllegalArgumentException("User already exists");
       }

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
        boolean isMailSent = emailService.sendEmail(user.getEmail(),user.getFullName() , EmailTemplateName.ACTIVATE_ACCOUNT , activationUrl , newToken , "Account Activation");

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

    public Response<LoginResponse> loginUser(@Valid LoginRequest request) throws  Exception {
        var auth = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.getEmail() , request.getPassword()));

        var user = (User)auth.getPrincipal();
        log.trace("fetched User: {}",user.toString());


//        if user account is not enabled
        log.trace("user enabled; {}", user.isEnabled());
        if(!user.isEnabled()) {
            return Response.error("User account is not enabled", HttpStatus.UNAUTHORIZED);
        }

        Map<String , Object> claims = new HashMap<>();
        claims.put("username" , user.getFullName());

        var token = jwtService.generateToken(claims , user);
        LoginResponse loginResponse = LoginResponse.builder().token(token).build();

        return Response.success("User Logged in Successfully", loginResponse);
    }
}
