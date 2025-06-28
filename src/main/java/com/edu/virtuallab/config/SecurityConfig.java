package com.edu.virtuallab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/user/register",
                        "/user/login",
                        "/experiment/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable();

        return http.build();
    }
}

