package com.example.festOn.application.diary.dto;

import com.example.festOn.application.diary.entity.Diary;
import com.example.festOn.application.festival.entity.Festival;
import com.example.festOn.application.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiaryResponseDto {
    User user;
    Festival festival;
    Diary diary;

}
