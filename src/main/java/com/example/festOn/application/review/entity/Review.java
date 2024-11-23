package com.example.festOn.application.review.entity;

import com.example.festOn.application.common.BaseEntity;
import com.example.festOn.application.festival.entity.Festival;
import com.example.festOn.application.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body",length = 1000)
    private String body;

    @OneToMany(mappedBy = "review")
    @JsonIgnoreProperties({"review"})
    private List<ReviewImg> reviewImgList = new ArrayList<>();

    @Builder
    public Review(User user, Festival festival, String title, String body) {
        this.user=user;
        this.festival=festival;
        this.title=title;
        this.body=body;
    }
}
