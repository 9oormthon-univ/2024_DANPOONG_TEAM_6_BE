package com.example.festOn.common.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class RefreshTokenService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RefreshTokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Refresh Token 저장
    public void saveRefreshToken(String kakaoId, String refreshToken, long expiration) {
        try {
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(kakaoId, refreshToken, Duration.ofMillis(expiration));
        } catch (Exception e) {
            log.error("리프레시 토큰 저장 실패: {}", e.getMessage());
        }
    }

    // Refresh Token 조회
    public String getRefreshToken(String kakaoId) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return (String) valueOperations.get(kakaoId);
    }

    // Refresh Token 삭제
    public void deleteRefreshToken(String kakaoId) {
        redisTemplate.delete(kakaoId);
    }
}
