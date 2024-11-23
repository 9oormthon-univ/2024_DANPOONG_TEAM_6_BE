package com.example.festOn.application.festival.dao;

import com.example.festOn.application.festival.entity.Festival;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    @Query("SELECT f FROM Festival f " +
            "WHERE (:keyword IS NULL OR f.title LIKE %:keyword%) " +
            "AND (:startDate IS NULL OR f.start >= :startDate) " +
            "AND (:endDate IS NULL OR f.end <= :endDate) " +
            "AND (:region IS NULL OR f.region LIKE %:region%)")
    List<Festival> searchFestivals(@Param("keyword") String keyword,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("region") String region);

    @Query("SELECT f FROM Festival f WHERE f.start <= :givenDate AND f.end >= : givenDate")
    List<Festival> findAllByGivenDate(@Param("givenDate") LocalDate givenDate);
}
