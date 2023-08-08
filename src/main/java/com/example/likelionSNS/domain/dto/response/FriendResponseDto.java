package com.example.likelionSNS.domain.dto.response;

import com.example.likelionSNS.domain.entity.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendResponseDto {
    private String username;

    public static FriendResponseDto of(User friend) {
        return FriendResponseDto.builder()
                .username(friend.getUsername())
                .build();
    }
}
