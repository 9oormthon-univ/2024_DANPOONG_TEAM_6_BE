package com.example.festOn.application.diary.controller;

import com.example.festOn.application.diary.dto.DiaryRequestDto;
import com.example.festOn.application.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "일기 작성", description = "")
    @PostMapping(value = "/post", consumes = {"multipart/form-data"})
    public ResponseEntity<Long> saveDiary(@RequestPart DiaryRequestDto requestDto,
                                              @RequestPart(required = false) List<MultipartFile> diaryImages) {
        Long diaryId = diaryService.saveDiary(requestDto, diaryImages);
        return ResponseEntity.ok(diaryId);
    }
}
