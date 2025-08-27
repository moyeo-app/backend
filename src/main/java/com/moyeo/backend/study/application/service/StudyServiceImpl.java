package com.moyeo.backend.study.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogDailyAggregateDto;
import com.moyeo.backend.challenge.log.domain.ChallengeLogRepository;
import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;
import com.moyeo.backend.study.application.dto.*;
import com.moyeo.backend.study.application.validator.StudyCalendarValidator;
import com.moyeo.backend.study.domain.StudyCalendarRepository;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j(topic = "StudyService")
@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final ChallengeLogRepository logRepository;
    private final StudyCalendarRepository studyCalendarRepository;
    private final UserContextService userContextService;
    private final StudyCalendarValidator calendarValidator;

    @Override
    @Transactional
    public void aggregate(LocalDate date) {
        List<ChallengeLogDailyAggregateDto> list = logRepository.aggregateDailyByUser(date);
        log.info("총 공부 시간 집계 완료, date = {}", date);

        if (!list.isEmpty()) {
            studyCalendarRepository.upsertAll(list);
        } else {
            log.info("집계 데이터 없음, date = {}", date);
        }

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            log.info("주간 학습 통계 처리 시도, weekEndDate = {}", date);
            upsertWeeklyStat(date);
        }
    }

    private void upsertWeeklyStat(LocalDate weekEndDate) {
        LocalDate monday = weekEndDate.with(DayOfWeek.MONDAY);

        List<WeeklyAgg> list = studyCalendarRepository.findWeeklyAgg(monday, weekEndDate);
        if (list.isEmpty()) return;

        List<RoutineStatReadResponseDto> stats = list.stream()
                .map(this::computeWeeklyAgg)
                .toList();
    }

    private RoutineStatReadResponseDto computeWeeklyAgg(WeeklyAgg agg) {
        int[] byDay = new int[]{ agg.mon(), agg.tue(), agg.wed(), agg.thu(), agg.fri(), agg.sat(), agg.sun() };
        int maxIdx = IntStream.range(1, 7)
                .reduce(0, (best, i) -> byDay[i] > byDay[best] ? i : best);   // 동률이면 기존 best 유지(월→일 우선)
        int minIdx = IntStream.range(1, 7)
                .reduce(0, (best, i) -> byDay[i] < byDay[best] ? i : best);

        DayOfWeek focusDay = DayOfWeek.values()[maxIdx];
        DayOfWeek leastDay = DayOfWeek.values()[minIdx];
        List<DayOfWeek> highAttendanceDays = IntStream.range(0, 7)
                .boxed()
                .filter(i -> byDay[i] > 0)
                .sorted((a, b) -> {
                    int cmp = Integer.compare(byDay[b], byDay[a]);
                    return (cmp != 0) ? cmp : Integer.compare(a, b);
                })
                .limit(3)
                .map(i -> DayOfWeek.values()[i])
                .toList();

        return RoutineStatReadResponseDto.builder()
                .userId(agg.userId())
                .startDate(agg.startDate())
                .totalMinutes(agg.totalMinutes())
                .avgMinutes(agg.avgMinutes())
                .focusDay(focusDay)
                .leastDay(leastDay)
                .highAttendanceDays(highAttendanceDays)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public StudyCalendarReadResponseDto getCalendar(StudyCalendarReadRequestDto requestDto) {
        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();

        DateRange range = calendarValidator.rangeForCalendar(requestDto);
        if (range.from() == null) {
            return StudyCalendarReadResponseDto.builder()
                    .days(Collections.emptyList())
                    .build();
        }

        LocalDate from = range.from();
        LocalDate to = range.to();

        List<DailyStudyDto> list = studyCalendarRepository.findDailyTotals(userId, from, to);
        List<DailyStudyDto> days = fillMissingWithZero(from, to, list);

        return StudyCalendarReadResponseDto.builder()
                .days(days)
                .build();
    }

    private List<DailyStudyDto> fillMissingWithZero(LocalDate from, LocalDate to, List<DailyStudyDto> list) {
        Map<LocalDate, Integer> byDate = list.stream()
                .collect(Collectors.toMap(DailyStudyDto::date, DailyStudyDto::totalMinutes, (a, b) -> a));

        List<DailyStudyDto> result = new ArrayList<>();
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            int minutes = byDate.getOrDefault(date, 0);
            result.add(DailyStudyDto.builder().date(date).totalMinutes(minutes).build());
        }
        return result;
    }
}
