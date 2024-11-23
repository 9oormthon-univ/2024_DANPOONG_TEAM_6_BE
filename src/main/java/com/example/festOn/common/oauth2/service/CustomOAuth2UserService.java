package com.example.festOn.common.oauth2.service;

import com.example.festOn.application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            Map<String, Object> attributes = oAuth2User.getAttributes();

            String kakaoId = String.valueOf(attributes.get("id"));

            boolean isFirstLogin = userService.findByKakaoId(kakaoId) == null;

            Map<String, Object> additionalAttributes = new HashMap<>(attributes);
            additionalAttributes.put("isFirstLogin", isFirstLogin);

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    additionalAttributes,
                    "id"
            );
        } catch (OAuth2AuthenticationException e) {
            log.warn("OAuth2 인증 실패: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.warn("사용자 정보 가져오기 실패: {}", e.getMessage());
            throw e;
        }
    }
}