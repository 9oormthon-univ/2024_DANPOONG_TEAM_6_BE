package com.example.festOn.application.diary.dao;

import com.example.festOn.application.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
