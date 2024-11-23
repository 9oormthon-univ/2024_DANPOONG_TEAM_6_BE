package com.example.festOn.application.review.dao;

import com.example.festOn.application.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
