package com.example.festOn.application.user.controller;

import com.example.festOn.application.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "회원")
public class UserController {

    private final UserService userService;
    @Operation(summary = "회원가입", description = "첫 로그인 사용자는 회원가입을 진행합니다.")
    @PostMapping ("/signup")
    public ResponseEntity<String> saveUser(@RequestParam("nickname") String nickname,
                                         @RequestPart(value = "userImg", required = false) MultipartFile userImg) {
        String kakaoId = userService.save(nickname, userImg);
        return ResponseEntity.ok(kakaoId);
    }

    @Operation(summary = "회원 탈퇴", description = "탈퇴시 회원과 관련된 정보가 삭제됩니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        String kakaoId = userService.deleteUser();
        return ResponseEntity.ok(kakaoId);
    }

    @Operation(summary = "회원 정보 조회", description = "마이페이지 메인에서 회원 정보를 조회합니다. 프로필 사진, 닉네임, 나의 축제 방문 기록을 반환합니다.")
    @DeleteMapping("/my")
    public ResponseEntity<String> getUser() {
        String kakaoId = userService.deleteUser();
        return ResponseEntity.ok(kakaoId);
    }
}
