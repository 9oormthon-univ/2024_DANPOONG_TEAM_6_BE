package com.example.festOn.application.diary.dao;

import com.example.festOn.application.diary.entity.DiaryImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryImgRepository extends JpaRepository<DiaryImg, Long> {
    @Query("SELECT d.imgUrl FROM DiaryImg d WHERE d.diary.id = :diaryId")
    List<String> findImgUrlByDiaryId(@Param("diaryId") Long diaryId);
}
