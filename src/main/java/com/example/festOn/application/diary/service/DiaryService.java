package com.example.festOn.application.diary.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.festOn.application.diary.dao.DiaryImgRepository;
import com.example.festOn.application.diary.dao.DiaryRepository;
import com.example.festOn.application.diary.dto.CreateDiaryRequest;
import com.example.festOn.application.diary.entity.Diary;
import com.example.festOn.application.diary.entity.DiaryImg;
import com.example.festOn.application.festival.dao.FestivalRepository;
import com.example.festOn.application.festival.entity.Festival;
import com.example.festOn.application.review.dto.CreateReviewRequest;
import com.example.festOn.application.review.entity.Review;
import com.example.festOn.application.review.entity.ReviewImg;
import com.example.festOn.application.user.entity.User;
import com.example.festOn.application.user.service.UserService;
import com.example.festOn.common.exception.UserNotFoundException;
import com.example.festOn.common.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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


}
