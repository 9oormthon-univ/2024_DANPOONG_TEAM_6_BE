package com.example.festOn.common.auth.service;

import com.example.festOn.application.user.dto.KakaoUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoService {
    public KakaoUserInfo getUserInfo(String accessToken) {
        log.info("Sending request to Kakao API with token: {}", accessToken);

        // 요청 헤더에 Authorization: Bearer {accessToken} 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // 카카오 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    KakaoUserInfo.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Kakao API response: {}", response.getBody());
                return response.getBody();
            } else {
                log.error("Error response from Kakao API: {}", response.getStatusCode());
                throw new RuntimeException("Failed to get user info from Kakao");
            }
        } catch (Exception e) {
            log.error("Error during Kakao API request: {}", e.getMessage());
            throw new RuntimeException("Error during Kakao API request");
        }
    }
}