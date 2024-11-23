package com.example.festOn.application.diary.dao;

import com.example.festOn.application.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
