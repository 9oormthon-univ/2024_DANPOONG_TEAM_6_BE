package com.example.festOn.application.user.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UserRequestDto(String nickname, String kakaoId, MultipartFile userImg) {
}
