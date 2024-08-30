package com.akshay.book.authentication;

import com.akshay.book.response.Response;
import com.akshay.book.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;

    public Response<String> registerUser(RegisterRequest request) throws  Exception{
       Response response = new Response();

       var userRole  =  roleRepository.findByName("USER");

       var user  = User.builder().


    }
}
