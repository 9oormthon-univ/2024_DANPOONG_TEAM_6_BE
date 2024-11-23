package com.example.festOn.application.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String kakaoId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String userImg;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean alarm;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean nightAlarm;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean diaryAlarm;
}
