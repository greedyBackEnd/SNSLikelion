package com.example.likelionSNS.domain.controller;

import com.example.likelionSNS.domain.dto.request.UserLoginDto;
import com.example.likelionSNS.domain.dto.request.UserRegistrationDto;
import com.example.likelionSNS.domain.entity.user.CustomUserDetails;
import com.example.likelionSNS.jwt.JwtTokenDto;
import com.example.likelionSNS.jwt.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    // 유저 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto dto) {
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .build();
        try {
            manager.createUser(userDetails);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 유저 로그인
    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@RequestBody UserLoginDto dto) {
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        JwtTokenDto response = new JwtTokenDto();
        response.setToken(jwtTokenUtils.generateToken(userDetails));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저 로그아웃
    @GetMapping("/logout")
    private ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return new ResponseEntity<>("logout success", HttpStatus.OK);
    }
}
