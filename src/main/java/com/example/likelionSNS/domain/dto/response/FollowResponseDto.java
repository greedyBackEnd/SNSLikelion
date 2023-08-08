package com.example.likelionSNS.domain.dto.response;

import com.example.likelionSNS.domain.entity.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponseDto {
    private Long id;
    private String username;
    private String email;

    public static FollowResponseDto of(User user) {
        return FollowResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
