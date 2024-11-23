package com.example.festOn.application.diary.dto;

import com.example.festOn.application.diary.entity.Diary;
import com.example.festOn.application.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class DiaryResponseDto {
    private String nickname;
    private String userImg;
    private Long festivalId;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private List<String> diaryImgList;
}
