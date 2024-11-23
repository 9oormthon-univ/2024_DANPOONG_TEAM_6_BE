package com.example.festOn.application.diary.service;

import com.example.festOn.application.diary.dao.DiaryImgRepository;
import com.example.festOn.application.diary.dao.DiaryRepository;
import com.example.festOn.application.diary.dto.CreateDiaryRequest;
import com.example.festOn.application.diary.entity.Diary;
import com.example.festOn.application.diary.entity.DiaryImg;
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
    private final UserService userService;
    private final S3Service s3Service;

    @Transactional
    public Long saveDiary(CreateDiaryRequest requestDto, List<MultipartFile> diaryImages) {
        User user = userService.getCurrentUser();

        if(user == null) {
            throw new UserNotFoundException("현재 사용자가 존재하지 않습니다.");
        }

        Diary diary = Diary.builder()
                .title(requestDto.getTitle())
                .body(requestDto.getBody())
                .festival(requestDto.getFestival())
                .build();

        diaryRepository.save(diary);
        Long diaryId = diary.getId();

        if(diaryImages != null) {
            for(int i=0; i<diaryImages.size(); i++) {
                MultipartFile imageFile = diaryImages.get(i);
                String photoUrl = "";
                if (imageFile != null && !imageFile.isEmpty()) {
                    // 이미지 파일 업로드 후 URL 획득
                    photoUrl = s3Service.uploadFile(imageFile, "diary");
                }

                DiaryImg diaryImg = DiaryImg.builder()
                        .diary(diary)
                        .imgUrl(photoUrl)
                        .build();

                diaryImgRepository.save(diaryImg);
            }
        }
        return diaryId;
    }


}
