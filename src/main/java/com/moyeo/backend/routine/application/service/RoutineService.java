package com.moyeo.backend.routine.application.service;

import com.moyeo.backend.routine.application.dto.RoutineReadRequestDto;
import com.moyeo.backend.routine.application.dto.RoutineReportReadResponseDto;
import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;

import java.time.LocalDate;

public interface RoutineService {

    void upsertWeeklyStat(LocalDate date);

    RoutineStatReadResponseDto getRoutineStat(RoutineReadRequestDto requestDto);

    void upsertWeeklyReport(LocalDate date);

    RoutineReportReadResponseDto getRoutineReport(RoutineReadRequestDto requestDto);
}
