package com.example.festOn.application.diary.controller;

import com.example.festOn.application.diary.dto.CreateDiaryRequest;
import com.example.festOn.application.diary.service.DiaryService;
import com.example.festOn.application.review.dto.CreateReviewRequest;
import com.example.festOn.application.user.entity.User;
import com.example.festOn.application.user.service.UserService;
import com.example.festOn.common.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
@Tag(name = "Diary", description = "일기")
public class DiaryController {

    private final DiaryService diaryService;
    private final UserService userService;
    private final S3Service s3Service;

    @Operation(summary = "Create", description = "일기 작성")
    @PostMapping(value = "/add")
    public ResponseEntity<String> saveDiary(@Parameter(description = "파일은 files로, ", required = true)
                                              @Valid @ModelAttribute CreateDiaryRequest createDiaryRequest) {
        User user = userService.getCurrentUser();

        List<String> imageUrlList = new ArrayList<>();
        List<MultipartFile> fileList = createDiaryRequest.files();
        if (createDiaryRequest.files()!= null && !createDiaryRequest.files().isEmpty()) {
            for (MultipartFile file : fileList) {
                String uploadedUrl = s3Service.uploadFile(file, "diary");
                imageUrlList.add(uploadedUrl); // 업로드된 파일 URL 저장
            }
        }

        diaryService.saveDiary(user, imageUrlList, createDiaryRequest);
        return ResponseEntity.ok().body("diary created");
    }
}