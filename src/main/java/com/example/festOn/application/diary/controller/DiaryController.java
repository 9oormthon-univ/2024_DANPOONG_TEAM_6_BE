package com.example.festOn.application.diary.controller;

import com.example.festOn.application.diary.dto.CreateDiaryRequest;
import com.example.festOn.application.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
@Tag(name = "Diary", description = "일기")
public class DiaryController {

    private final DiaryService diaryService;

//    @Operation(summary = "일기 작성", description = "")
//    @PostMapping(value = "/post")
//    public ResponseEntity<Long> saveDiary(@Valid ) {
//        Long diaryId = diaryService.saveDiary(requestDto, diaryImages);
//        return ResponseEntity.ok(diaryId);
    }
}