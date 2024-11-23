package com.example.festOn.application.review.dao;

import com.example.festOn.application.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByFestivalId(Long festival_id);
}
