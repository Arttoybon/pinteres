package com.example.pinteres.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

            .formLogin(form -> form.disable()); // Desactiva el formulario por defecto de Spring
            
        return http.build();
    }
}
