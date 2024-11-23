package com.example.festOn.application.festival.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Festival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private String title;

    private String festivalImg;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    @Column(length = 1000)
    private String body;

    private String region;

    private String location;

    @Builder
    public Festival(String title, String festivalImg,  LocalDate start, LocalDate end, String body,
                    String region, String location) {
        this.title = title;
        this.festivalImg = festivalImg;
        this.start = start;
        this.end = end;
        this.body = body;
        this.region = region;
        this.location = location;
    }
}
