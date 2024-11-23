package com.example.festOn.common.auth.filter;

import com.example.festOn.common.auth.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/auth/kakao") || request.getRequestURI().startsWith("/api/user/signup")) {
            filterChain.doFilter(request, response);  // 필터를 건너뛰고 다음 필터로 진행
            return;
        }

        // 헤더에서 AccessToken을 추출
        String accessToken = getTokenFromRequest(request);

        if (accessToken != null) {
            log.info("Authorization 헤더에서 토큰을 추출했습니다: {}", accessToken);  // 토큰 추출 로그

            // 토큰이 유효한지 검증
            if (jwtTokenProvider.validateToken(accessToken)) {
                log.info("Access token이 유효합니다.");

                if (jwtTokenProvider.isTokenExpired(accessToken)) {
                    // Access Token이 만료된 경우
                    log.error("Access token이 만료되었습니다.");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired");
                    return;
                } else {
                    // Access Token이 만료되지 않은 경우
                    log.info("Access token이 만료되지 않았습니다. 인증 컨텍스트를 설정합니다.");
                    setAuthenicationContext(accessToken, request);
                }
            } else {
                // Access Token이 유효하지 않은 경우
                log.error("유효하지 않은 Access token입니다.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Access token");
                return;
            }
        } else {
            log.warn("Authorization 헤더에서 토큰이 발견되지 않았습니다.");
        }

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }

    private void setAuthenicationContext(String token, HttpServletRequest request) {
        // JWT에서 사용자 정보 추출
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        log.info("JWT에서 추출한 사용자 ID: {}", userId);  // 사용자 ID 추출 로그

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

        // 인증 정보 설정
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("인증 정보가 SecurityContext에 설정되었습니다.");  // 인증 설정 완료 로그
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // Authorization 헤더에서 Bearer 토큰 추출
        String header = request.getHeader("Authorization");

        // Bearer Token이 존재하는지 확인
        if (header != null && header.startsWith("Bearer ")) {
            // "Bearer " 접두사를 제거하고 토큰 반환
            log.info("Authorization 헤더에서 Bearer 토큰을 찾았습니다.");
            return header.substring(7);
        }
        return null;
    }
}
