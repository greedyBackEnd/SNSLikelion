package com.example.likelionSNS.domain.dto.request;

import com.example.likelionSNS.domain.entity.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRegistrationDto {
    private String username;
    private String password;
    private String phone;
    private String email;
    private String address;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .phone(phone)
                .email(email)
                .address(address)
                .build();
    }
}
