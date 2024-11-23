package com.example.festOn.application.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserResponseDto {
    private UserDto user;
    private List<FestivalDto> festivalDtoList;

    @Getter
    @Builder
    public static class UserDto {
        private String kakaoId;
        private String userImg;
        private String nickname;
    }

    @Getter
    @Builder
    public static class FestivalDto {
        private Long id;
        private String festivalImg;
        private String title;
        private String body;
    }
}
