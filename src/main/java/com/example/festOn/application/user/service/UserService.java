package com.example.festOn.application.user.service;

import com.example.festOn.application.user.dao.UserRepository;
import com.example.festOn.application.user.dto.UserRequestDto;
import com.example.festOn.application.user.entity.User;
import com.example.festOn.common.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public String save(UserRequestDto dto) {
        String kakaoId = dto.kakaoId();
        String userImg = s3Service.uploadFile(dto.userImg(), "profile");

        User newUser = User.builder()
                .kakaoId(kakaoId)
                .nickname(dto.nickname())
                .userImg(userImg)
                .alarm(true)
                .diaryAlarm(true)
                .nightAlarm(true)
                .build();

        return userRepository.save(newUser).getKakaoId();
    }

    @Transactional
    public String deleteUser() {
        User user = getCurrentUser();
        userRepository.delete(user);
        // SecurityContext 초기화 (탈퇴 후 더 이상 인증되지 않도록)
        SecurityContextHolder.clearContext();
        return user.getKakaoId();
    }

    public User findByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId).orElse(null);
    }

    public User getCurrentUser() {
        String kakaoId = getCurrentUserId();
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof OAuth2User) {
            return String.valueOf(((OAuth2User) principal).getAttributes().get("id"));
        } else {
            return principal.toString();
        }
    }
}