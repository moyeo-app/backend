package com.moyeo.backend.study.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogDailyAggregateDto;
import com.moyeo.backend.challenge.log.domain.ChallengeLogRepository;
import com.moyeo.backend.study.application.dto.DailyStudyDto;
import com.moyeo.backend.study.application.dto.DateRange;
import com.moyeo.backend.study.application.dto.StudyCalendarReadRequestDto;
import com.moyeo.backend.study.application.dto.StudyCalendarReadResponseDto;
import com.moyeo.backend.study.application.validator.StudyCalendarValidator;
import com.moyeo.backend.study.domain.StudyCalendarRepository;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        log.info("총 공부 시간 집계 완료, data = {}", list);
        if (list.isEmpty()) return;
        studyCalendarRepository.upsertAll(list);
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
