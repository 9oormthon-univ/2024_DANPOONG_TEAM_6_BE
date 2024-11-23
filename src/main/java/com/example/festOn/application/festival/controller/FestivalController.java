package com.example.festOn.application.festival.controller;

import com.example.festOn.application.festival.entity.Festival;
import com.example.festOn.application.festival.service.FestivalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Festival", description = "축제")
@RequestMapping("/festival")
public class FestivalController {
    private final FestivalService festivalService;

    @GetMapping("/testCrawl")
    public void testCrawling() throws IOException {
        festivalService.crawlFestival();
    }

    @Operation(summary = "축제 검색", description = "검색어, 날짜, 지역으로 축제를 검색합니다")


    @GetMapping("/search")
    public ResponseEntity<List<Festival>> searchFestivals(
            @Parameter(name="keyword", example = "검색어")
            @RequestParam(required = false) String keyword,
            @Parameter(name="startDate", example = "2024-02-26")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(name="endDate", example = "2024-11-23")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(name = "region", example = "지역 관련 키워드 서울시, 전라남도 등")
            @RequestParam(required = false) String region) {
        return ResponseEntity.ok().body(festivalService.searchFestivals(keyword, startDate, endDate, region));
    }
}