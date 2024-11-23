package com.example.festOn.application.festival.service;

import com.example.festOn.application.festival.dao.FestivalRepository;
import com.example.festOn.application.festival.entity.Festival;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FestivalService {
    private final FestivalRepository festivalRepository;


    public List<Festival> searchFestivals(String keyword, LocalDate startDate, LocalDate endDate, String region) {
        return festivalRepository.searchFestivals(keyword, startDate, endDate, region);
    }

    public List<Festival> findAllByGivenDate(LocalDate givenDate) {
        return festivalRepository.findAllByGivenDate(givenDate);
    }

    public Optional<Festival> findById(Long id) {
        return festivalRepository.findById(id);
    }

    public List<Festival> findAllBeforeStart(LocalDate givenDate) {
        return festivalRepository.findAllBeforeStart(givenDate);
    }

    public void crawlFestival() throws IOException {
        String festivalURL = "https://www.mcst.go.kr/kor/s_culture/festival/festivalList.jsp";
        int currentPage = 1;
        boolean flag = true;

        while (flag) { // 페이지 반복 시작
            String pageURL = festivalURL + "?pCurrentPage=" + currentPage;
            System.out.println("Fetching page: " + pageURL);
            flag = false;

            Document doc = Jsoup.connect(pageURL).get();
            Elements links = doc.select("li a");

            for (Element link : links) {
                String url = link.absUrl("href");
                if (!url.contains("festivalView.jsp")) {
                    continue;
                }

                System.out.println("Fetching data from: " + url);
                flag = true;

                Document festivalDoc = Jsoup.connect(url).get();

                // 데이터 추출
                String title = festivalDoc.selectFirst("div.view_title") != null
                        ? festivalDoc.selectFirst("div.view_title").text()
                        : "";
                String imageUrl = festivalDoc.selectFirst("div.culture_view_img img") != null
                        ? festivalDoc.selectFirst("div.culture_view_img img").absUrl("src")
                        : "";
                String region = getElementTextByLabel(festivalDoc, "개최지역");
                String dateTime = getElementTextByLabel(festivalDoc, "개최기간");
                String location = getElementTextByLabel(festivalDoc, "축제장소");
                String body = festivalDoc.selectFirst("div.view_con") != null
                        ? festivalDoc.selectFirst("div.view_con").text()
                        : "";

                // 날짜 파싱 로직 유지
                LocalDate startDate = null;
                LocalDate endDate = null;
                if (dateTime.contains("~")) {
                    // | 뒷부분 제거 및 공백 제거
                    dateTime = dateTime.split("\\|")[0].replaceAll("\\s+", "").trim();

                    // .0 제거
                    dateTime = dateTime.replaceAll("\\.0", ".");

                    // 날짜 범위를 "~"로 구분
                    String[] dates = dateTime.split("~");

                    if (dates.length > 1) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M.d.");
                        try {
                            // 시작 날짜 처리: ~ 앞에 . 추가
                            String startRaw = dates[0].trim();
                            if (!startRaw.endsWith(".")) {
                                startRaw += ".";
                            }
                            startDate = LocalDate.parse(startRaw, formatter);

                            // 종료 날짜 처리: ~ 뒤에 날짜 정리 및 . 추가
                            String rawEndDate = dates[1].trim();
                            if (!rawEndDate.endsWith(".")) {
                                rawEndDate += ".";
                            }
                            if (!rawEndDate.matches("\\d{4}\\.\\d{1,2}\\.\\d{1,2}\\.")) {
                                rawEndDate = startDate.getYear() + "." + rawEndDate;
                            }
                            endDate = LocalDate.parse(rawEndDate.trim(), formatter);
                        } catch (Exception e) {
                            System.out.println("Error parsing dates: " + dateTime);
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M.d.");
                            String singleDate = dates[0].trim();
                            if (!singleDate.endsWith(".")) {
                                singleDate += ".";
                            }
                            startDate = LocalDate.parse(singleDate, formatter);
                        } catch (Exception e) {
                            System.out.println("Error parsing start date: " + dateTime);
                            e.printStackTrace();
                        }
                    }
                }

                // Debug
                System.out.println("Title: " + title);
                System.out.println("Image URL: " + imageUrl);
                System.out.println("Region: " + region);
                System.out.println("Date & Time: " + dateTime);
                System.out.println("Start Date: " + startDate);
                System.out.println("End Date: " + endDate);
                System.out.println("Location: " + location);
                System.out.println("Description: " + body);
                System.out.println("---------------------------------------------------");

                //실제 save
                /*Festival festival = Festival.builder()
                        .festivalImg(imageUrl)
                        .title(title)
                        .body(body)
                        .start(startDate)
                        .end(endDate)
                        .region(region)
                        .location(location)
                        .build();
                festivalRepository.save(festival);*/
            }
            currentPage++;
        }
    }

    private String getElementTextByLabel(Document doc, String label) {
        Elements dtElements = doc.select("dt");
        for (Element dt : dtElements) {
            if (dt.text().equals(label)) {
                Element dd = dt.nextElementSibling(); // dt 태그의 다음 형제 태그 (dd)를 가져옴
                if (dd != null && dd.tagName().equals("dd")) {
                    return dd.text();
                }
            }
        }
        return ""; // 기본값 반환
    }
}