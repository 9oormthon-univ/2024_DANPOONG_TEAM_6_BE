package com.example.festOn.application.review.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.festOn.application.festival.dao.FestivalRepository;
import com.example.festOn.application.festival.entity.Festival;
import com.example.festOn.application.review.dao.ReviewImgRepository;
import com.example.festOn.application.review.dao.ReviewRepository;
import com.example.festOn.application.review.dto.CreateReviewRequest;
import com.example.festOn.application.review.dto.ReviewResponse;
import com.example.festOn.application.review.entity.Review;
import com.example.festOn.application.review.entity.ReviewImg;
import com.example.festOn.application.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final FestivalRepository festivalRepository;

    @Transactional
    public void saveReview(User user, List<String> imageUrlList, CreateReviewRequest createReviewRequest) {
        Festival festival = festivalRepository.findById(createReviewRequest.festival_id())
                .orElseThrow(()->new NotFoundException("Festival Not Found"));;
        Review review = Review.builder()
                .user(user)
                .festival(festival)
                .title(createReviewRequest.title())
                .body(createReviewRequest.body())
                .build();


        for (String imageUrl : imageUrlList) {
            ReviewImg reviewImg = ReviewImg.builder()
                    .review(review)
                    .imgUrl(imageUrl)
                    .build();
            reviewImgRepository.save(reviewImg);
        }

        reviewRepository.save(review);
    }

    public List<Review> findAllByFestivalId(Long festival_id) {
        List<Review> reviewList = reviewRepository.findAllByFestivalId(festival_id);


        return reviewList;
    }

}
