package com.example.festOn.common.auth.filter;

import com.example.festOn.common.auth.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
        String accessToken =getTokenFromRequest(request);
        if(accessToken != null) {
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
        filterChain.doFilter(request, response);
    }

    private void setAuthenicationContext(String token, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUserIdFromToken(token);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("AccessToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
