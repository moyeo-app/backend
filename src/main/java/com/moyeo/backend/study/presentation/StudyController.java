package com.moyeo.backend.study.presentation;

import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.study.application.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j(topic = "StudyController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/studies")
public class StudyController implements StudyControllerDocs {

    private final StudyService studyService;

    @PostMapping("/aggregate")
    public ResponseEntity<ApiResponse<Void>> aggreate(
            @RequestParam LocalDate from, @RequestParam LocalDate to) {
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            studyService.aggregate(date);
        }
        log.info("(Admin) 총 공부 시간 집계 Admin 으로 실행, from = {}, to = {}", from, to);
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
