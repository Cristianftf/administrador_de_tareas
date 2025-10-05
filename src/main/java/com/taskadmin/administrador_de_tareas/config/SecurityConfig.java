package com.taskadmin.administrador_de_tareas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configure(http)) // Enable CORS
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // Allow all requests to /api/**
                .requestMatchers("/h2-console/**").permitAll() // Allow H2 console access
                .anyRequest().authenticated() // Require authentication for other endpoints
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable()) // Allow H2 console to work in frames
            );

        return http.build();
    }
}
