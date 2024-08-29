package com.akshay.book.security;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Data
//for role based security
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.
                cors(withDefaults()).
                csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(requests ->
                    requests.requestMatchers("/auth/*").
                            permitAll().
                            anyRequest().authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
//        spring will not store session for every request our application will react like it does not know anything about that
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
