package com.example.likelionSNS.domain.dto.response;

import com.example.likelionSNS.domain.entity.user.FriendRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendRequestResponseDto {
    private String requestUsername;

    public static FriendRequestResponseDto of(FriendRequest friendRequest) {
        return FriendRequestResponseDto.builder()
                .requestUsername(friendRequest.getRequester().getUsername())
                .build();
    }
}
