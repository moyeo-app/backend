package com.moyeo.backend.study.application.service;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogDailyAggregateDto;
import com.moyeo.backend.challenge.log.domain.ChallengeLogRepository;
import com.moyeo.backend.study.domain.StudyCalendarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j(topic = "StudyService")
@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final ChallengeLogRepository logRepository;
    private final StudyCalendarRepository studyCalendarRepository;

    @Override
    @Transactional
    public void aggregate(LocalDate date) {
        List<ChallengeLogDailyAggregateDto> list = logRepository.aggregateDailyByUser(date);
        log.info("총 공부 시간 집계 완료, data = {}", list);
        if (list.isEmpty()) return;
        studyCalendarRepository.upsertAll(list);
    }
}
