package com.example.festOn.common.auth.controller;

import com.example.festOn.application.user.dto.KakaoUserInfo;
import com.example.festOn.application.user.entity.User;
import com.example.festOn.application.user.service.UserService;
import com.example.festOn.common.auth.jwt.JwtTokenProvider;
import com.example.festOn.common.auth.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증")
public class AuthController {

    private final KakaoService kakaoService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "카카오", description = "클라이언트에서 Authorization: Bearer {access_token} 형태로 엑세스 토큰을 받아 카카오 서버에 엑세스 토큰을 검증받습니다.")
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("Received Authorization header: {}", authorizationHeader);  // 헤더 출력

        if (!authorizationHeader.startsWith("Bearer ")) {
            log.error("Authorization header is missing 'Bearer' prefix");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization Header");
        }

        String kakaoAccessToken = authorizationHeader.substring(7); // "Bearer " 제거
        log.info("Received Kakao access token: {}", kakaoAccessToken);

        KakaoUserInfo userInfo;
        // 카카오에서 사용자 정보 가져오기
        try {
            userInfo = kakaoService.getUserInfo(kakaoAccessToken);
            log.info("Kakao user info retrieved: {}", userInfo);
        } catch (Exception e) {
            log.error("Failed to get user info from Kakao: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Kakao access token");
        }

        // 사용자 DB 조회
        User existingUser = userService.findByKakaoId(userInfo.getId());
        log.info("existingUser ? " + existingUser);
        if (existingUser == null) {
            // 새 사용자: 가입 필요
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse("SIGNUP_REQUIRED", userInfo));
        }

        log.info("existingUser : " + existingUser.getKakaoId());
        // 기존 사용자: JWT 발행
        String jwtToken = jwtTokenProvider.createToken(existingUser.getKakaoId());
        return ResponseEntity.ok(new AuthResponse("LOGIN_SUCCESS", jwtToken));
    }

    // 응답 객체
    @Getter
    @AllArgsConstructor
    public static class AuthResponse {
        private final String status;
        private final Object data;
    }
}
