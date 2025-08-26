package com.moyeo.backend.study.application.service;

import com.moyeo.backend.study.application.dto.StudyCalendarReadRequestDto;
import com.moyeo.backend.study.application.dto.StudyCalendarReadResponseDto;

import java.time.LocalDate;

public interface StudyService {
    void aggregate(LocalDate date);

    StudyCalendarReadResponseDto getCalendar(StudyCalendarReadRequestDto requestDto);
}
