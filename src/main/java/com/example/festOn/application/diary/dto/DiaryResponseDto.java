package com.example.festOn.application.diary.dto;

import com.example.festOn.application.festival.entity.Festival;
import com.example.festOn.application.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DiaryResponseDto {
    
    String title;
    String body;
    LocalDateTime createdAt;
    Festival festival;
    User user;
}