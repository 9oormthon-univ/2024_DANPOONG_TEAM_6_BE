package com.example.festOn.application.festival.controller;

import com.example.festOn.application.festival.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/festival")
public class FestivalController {
    private final FestivalService festivalService;

    @GetMapping("/testCrawl")
    public void testCrawling() throws IOException {
        festivalService.crawlFestival();
    }
}
