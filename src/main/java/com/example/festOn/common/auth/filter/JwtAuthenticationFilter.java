package com.example.festOn.common.auth.filter;

import com.example.festOn.common.auth.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 AccessToken을 추출
        String accessToken = getTokenFromRequest(request);

        if (accessToken != null) {
            // 토큰이 유효한지 검증
            if (jwtTokenProvider.validateToken(accessToken)) {
                if (jwtTokenProvider.isTokenExpired(accessToken)) {
                    // Access Token이 만료된 경우
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired");
                    return;
                } else {
                    // Access Token이 만료되지 않은 경우
                    setAuthenicationContext(accessToken, request);
                }
            } else {
                // Access Token이 유효하지 않은 경우
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Access token");
                return;
            }
        }

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }

    private void setAuthenicationContext(String token, HttpServletRequest request) {
        // JWT에서 사용자 정보 추출
        String userId = jwtTokenProvider.getUserIdFromToken(token);

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

        // 인증 정보 설정
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // Authorization 헤더에서 Bearer 토큰 추출
        String header = request.getHeader("Authorization");

        // Bearer Token이 존재하는지 확인
        if (header != null && header.startsWith("Bearer ")) {
            // "Bearer " 접두사를 제거하고 토큰 반환
            return header.substring(7);
        }
        return null;
    }
}