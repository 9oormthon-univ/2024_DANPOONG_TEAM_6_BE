package com.example.festOn.application.festival.dao;

import com.example.festOn.application.festival.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
}
