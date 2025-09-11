package com.moyeo.backend.study.presentation;

import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.study.application.dto.StudyCalendarReadRequestDto;
import com.moyeo.backend.study.application.dto.StudyCalendarReadResponseDto;
import com.moyeo.backend.study.application.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j(topic = "StudyController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/studies")
public class StudyController implements StudyControllerDocs {

    private final StudyService studyService;

    @Override
    @PostMapping("/aggregate")
    public ResponseEntity<ApiResponse<Void>> aggreate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            studyService.aggregate(date);
        }
        log.info("(Admin) 총 공부 시간 집계 Admin 으로 실행, from = {}, to = {}", from, to);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @Override
    @GetMapping("/days")
    public ResponseEntity<ApiResponse<StudyCalendarReadResponseDto>> getCalendar(
            @ParameterObject @ModelAttribute StudyCalendarReadRequestDto requestDto) {
        return ResponseEntity.ok().body(ApiResponse.success(studyService.getCalendar(requestDto)));
    }
}
