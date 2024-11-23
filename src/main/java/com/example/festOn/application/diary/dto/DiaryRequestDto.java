package com.example.festOn.application.diary.dto;

import com.example.festOn.application.festival.entity.Festival;
import lombok.Getter;

@Getter
public class DiaryRequestDto {
    private String title;
    private String body;
    private Festival festival;
}