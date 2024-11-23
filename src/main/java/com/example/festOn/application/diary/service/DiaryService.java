package com.example.festOn.application.diary.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.festOn.application.diary.dao.DiaryImgRepository;
import com.example.festOn.application.diary.dao.DiaryRepository;
import com.example.festOn.application.diary.dto.CreateDiaryRequest;
import com.example.festOn.application.diary.dto.DiaryResponseDto;
import com.example.festOn.application.diary.entity.Diary;
import com.example.festOn.application.diary.entity.DiaryImg;
import com.example.festOn.application.festival.dao.FestivalRepository;
import com.example.festOn.application.festival.entity.Festival;
import com.example.festOn.application.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DiaryImgRepository diaryImgRepository;
    private final FestivalRepository festivalRepository;

    @Transactional
    public void saveDiary(User user, List<String> imageUrlList, CreateDiaryRequest createDiaryRequest) {
        Festival festival = festivalRepository.findById(createDiaryRequest.festival_id())
                .orElseThrow(()->new NotFoundException("Festival Not Found"));;
        Diary diary = Diary.builder()
                .user(user)
                .festival(festival)
                .title(createDiaryRequest.title())
                .body(createDiaryRequest.body())
                .build();


        for (String imageUrl : imageUrlList) {
            DiaryImg diaryImg = DiaryImg.builder()
                    .diary(diary)
                    .imgUrl(imageUrl)
                    .build();
            diaryImgRepository.save(diaryImg);
        }

        diaryRepository.save(diary);
    }

    @Transactional
    public DiaryResponseDto getDiary(User user, Long diaryId) throws AccessDeniedException {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundException("Diary Not Found"));

        if(user != diary.getUser()) {
            throw new AccessDeniedException("접근할 수 없습니다.");
        }

        Festival festival = festivalRepository.findById(diary.getFestival().getId())
                .orElseThrow(()->new NotFoundException("Festival Not Found"));

        List<String> files = diaryImgRepository.findImgUrlByDiaryId(diaryId);

        return DiaryResponseDto.builder()
                .nickname(user.getNickname())
                .userImg(user.getUserImg())
                .festivalId(festival.getId())
                .title(diary.getTitle())
                .body(diary.getBody())
                .createdAt(diary.getCreatedAt())
                .diaryImgList(files)
                .build();
    }
}
