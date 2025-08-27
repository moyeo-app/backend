package com.moyeo.backend.study.infrastructure.repository;

import com.moyeo.backend.study.application.dto.DailyStudyDto;
import com.moyeo.backend.study.application.dto.WeeklyAgg;

import java.time.LocalDate;
import java.util.List;

public interface CustomStudyCalendarRepository {
    List<DailyStudyDto> findDailyTotals(String userId, LocalDate from, LocalDate to);

    List<WeeklyAgg> findWeeklyAgg(LocalDate monday, LocalDate weekEndDate);
}
