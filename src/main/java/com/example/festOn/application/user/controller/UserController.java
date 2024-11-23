package com.example.festOn.application.user.controller;

import com.example.festOn.application.review.dto.CreateReviewRequest;
import com.example.festOn.application.user.dto.UserRequestDto;
import com.example.festOn.application.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @PostMapping("/signup")
    public ResponseEntity<String> saveUser(@Parameter(description = "파일은 files로, ", required = true)
                                               @Valid @ModelAttribute UserRequestDto dto) {
        String kakaoId = userService.save(dto);
        return ResponseEntity.ok(kakaoId);
    }

    @Operation(summary = "회원 탈퇴", description = "탈퇴시 회원과 관련된 정보가 삭제됩니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        String kakaoId = userService.deleteUser();
        return ResponseEntity.ok(kakaoId);
    }
}
