package com.example.likelionSNS.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginDto {
    private String username;
    private String password;
}
