package com.example.festOn.application.festival.controller;

import com.example.festOn.application.festival.entity.Festival;
import com.example.festOn.application.festival.service.FestivalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@Tag(name = "Festival", description = "축제")
@RequestMapping("/festival")
public class FestivalController {
    private final FestivalService festivalService;

    /*@GetMapping("/testCrawl")
    public void testCrawling() throws IOException {
        festivalService.crawlFestival();
    }*/

    @Operation(summary = "id로 축제 조회", description = "축제 상세보기 ")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Festival>> findFestival(
            @Parameter(description = "Festival Id를 입력해주세요.", required = true) @PathVariable Long id) {
        return ResponseEntity.ok().body(festivalService.findById(id));
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
    @Operation(summary = "특정 날짜로 축제 검색", description = "특정 날짜에 진행 중인 축제 조회")
    @GetMapping("/calender")
    public ResponseEntity<List<Festival>> searchFestivalsOnGivenDate(
            @Parameter(name="givenDate", example = "2024-02-26")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate givenDate) {
        return ResponseEntity.ok().body(festivalService.findAllByGivenDate(givenDate));
    }

    @Operation(summary = "전체 조회", description = "현재 날짜 기준으로 진행 중 -> 진행 에정 순서로 조회")
    @GetMapping("/all")
    public ResponseEntity<List<Festival>> getFestivalList () {
        LocalDate now = LocalDate.now();
        List<Festival> currentList = festivalService.findAllByGivenDate(now);
        List<Festival> beforeList = festivalService.findAllBeforeStart(now);

        List<Festival> result = Stream.of(currentList, beforeList)
                .flatMap(x -> x.stream())
                .toList();

        return ResponseEntity.ok().body(result);
    }
}
