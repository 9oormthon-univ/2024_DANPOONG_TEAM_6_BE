package com.example.festOn.application.user.service;

import com.example.festOn.application.user.dao.UserRepository;
import com.example.festOn.application.user.entity.User;
import com.example.festOn.common.auth.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    @Value("${spring.cloud.aws.s3.default-profile-img}")
    private String defaultImg;

    @Transactional
    public Long save(String nickname, MultipartFile userImgFile) {
        String kakaoId = getCurrentUserId();
        Optional<User> existingUser = userRepository.findByKakaoId(kakaoId);

        String userImg = "";
        if(userImgFile.isEmpty()) userImg = defaultImg;
        else userImg = s3Service.uploadFile(userImgFile, "profile");

        if(existingUser.isEmpty()) {
            User user = existingUser.get();
            user.setNickname(nickname);
            user.setUserImg(userImg);
            return userRepository.save(user).getId();
        }

        User newUser = User.builder()
                .kakaoId(kakaoId)
                .nickname(nickname)
                .userImg(userImg)
                .alarm(true)
                .diaryAlarm(true)
                .nightAlarm(true)
                .build();

        return userRepository.save(newUser).getId();
    }

    @Transactional
    public Long deleteUser() {
        User user = getCurrentUser();
        userRepository.delete(user);
        refreshTokenService.deleteRefreshToken(user.getKakaoId());
        return user.getId();
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