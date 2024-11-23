package com.example.festOn.application.review.dto;

import com.example.festOn.application.review.entity.Review;
import com.example.festOn.application.review.entity.ReviewImg;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ReviewResponse(Review review, List<ReviewImg> reviewImgList) {
}
