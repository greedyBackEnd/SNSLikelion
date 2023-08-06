package com.example.likelionSNS.config;

import com.example.likelionSNS.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, AuthorizationFilter.class)
                .authorizeHttpRequests(
                        authHttp -> authHttp
                                .requestMatchers("/api/v1/auth/**", "/token/issue").permitAll()
                                .requestMatchers("/api/v1/users/**").authenticated()
                                .requestMatchers( HttpMethod.GET,"/api/v1/feeds/**").permitAll()
                                .requestMatchers( HttpMethod.GET,"/api/v1/feeds/user/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/v2/items/**").permitAll()
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                )
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
