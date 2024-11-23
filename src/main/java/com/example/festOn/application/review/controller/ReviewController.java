package com.example.festOn.application.review.controller;

import com.example.festOn.application.review.dto.CreateReviewRequest;
import com.example.festOn.application.review.service.ReviewService;
import com.example.festOn.application.user.entity.User;
import com.example.festOn.application.user.service.UserService;
import com.example.festOn.common.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "소감")
@RequestMapping("/review")
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final S3Service s3Service;


    @Operation(summary = "Create", description = "소감 올리기")
    @PostMapping("/add")
    public ResponseEntity <String> createReview (@Valid @ModelAttribute CreateReviewRequest createReviewRequest) {

        User user = userService.getCurrentUser();

        List<String> imageUrlList = new ArrayList<>();
        List<MultipartFile> fileList = createReviewRequest.files();
        if (createReviewRequest.files()!= null && !createReviewRequest.files().isEmpty()) {
            for (MultipartFile file : fileList) {
                String uploadedUrl = s3Service.uploadFile(file, "review");
                imageUrlList.add(uploadedUrl); // 업로드된 파일 URL 저장
            }
        }

        reviewService.saveReview(user, imageUrlList, createReviewRequest);
        return ResponseEntity.ok().body("review created");
    }
}
