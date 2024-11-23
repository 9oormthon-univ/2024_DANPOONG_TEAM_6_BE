package com.example.festOn.application.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImg {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name="review_id")
    private Review review;

    private String imgUrl;

    @Builder
    ReviewImg(Review review, String imgUrl) {
        this.review = review;
        this.imgUrl = imgUrl;
    }
}
