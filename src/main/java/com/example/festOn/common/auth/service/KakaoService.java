package com.example.festOn.common.auth.service;

import com.example.festOn.application.user.dto.KakaoUserInfo;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    public KakaoUserInfo getUserInfo(String accessToken) {
        // 요청 헤더에 Authorization: Bearer {accessToken} 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // 카카오 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                KAKAO_USER_INFO_URL,
                HttpMethod.GET,
                entity,
                KakaoUserInfo.class
        );

        return response.getBody();
    }
}