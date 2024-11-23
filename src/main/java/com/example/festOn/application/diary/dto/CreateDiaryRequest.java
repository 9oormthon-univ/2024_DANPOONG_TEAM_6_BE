package com.example.festOn.application.diary.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record CreateDiaryRequest(Long festival_id, String title, String body, List<MultipartFile> files) {
}