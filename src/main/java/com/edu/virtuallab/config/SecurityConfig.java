package com.edu.virtuallab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

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
                        "/experiment/**",
                        "/api/upload/image",   // 加上上传路径，放行
                        "/upload/**"   // ✅ 添加这一行

                ).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ✅ 放行预检请求

                .anyRequest().authenticated()
                .and()
                .formLogin().disable();

        return http.build();
    }
}

