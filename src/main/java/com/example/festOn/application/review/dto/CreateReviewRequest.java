package com.example.festOn.application.review.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record CreateReviewRequest(Long festival_id, String title, String body, List<MultipartFile> files) {
}
