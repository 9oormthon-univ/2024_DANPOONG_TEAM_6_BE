package com.example.festOn.common.auth.api;

import com.example.festOn.common.auth.jwt.JwtTokenProvider;
import com.example.festOn.common.auth.service.RefreshTokenService;
import com.example.festOn.common.config.UrlProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
@Tag(name = "Token", description = "토큰")
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UrlProperties urlProperties;

    @Operation(summary = "액세스 토큰 재발급", description = "RefreshToken을 통해 엑세스 토큰 발급")
    @PostMapping("/reissue")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromCookies(request);
        if(refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Refresh Token provided");
        }

        if(!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or Expired Refresh Token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        String savedRefreshToken = refreshTokenService.getRefreshToken(userId);
        if(savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token mismatch");
        }

        String newAccessToken = jwtTokenProvider.createToken(userId);
        addAccessTokenToCookie(response, newAccessToken);

        return ResponseEntity.ok("Access Token reissued successfully");
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromCookies(request);

        if(refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            String kakaoId = jwtTokenProvider.getUserIdFromToken(refreshToken);
            refreshTokenService.deleteRefreshToken(kakaoId);

            deleteCookie(response, "AccessToken");
            deleteCookie(response, "RefreshToken");

            return ResponseEntity.ok("Successfully logged out");
        }

        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Invalid token");
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getValue().equals("RefreshToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void addAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from("AccessToken", accessToken)
                .maxAge((int) jwtTokenProvider.getValidityInMilliseconds() / 1000)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .domain(urlProperties.getDomain())
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void deleteCookie(HttpServletResponse response, String cookieName) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .domain(urlProperties.getDomain())
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}