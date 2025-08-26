package com.moyeo.backend.study.domain;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogDailyAggregateDto;
import com.moyeo.backend.study.application.dto.DailyStudyDto;

import java.time.LocalDate;
import java.util.List;

public interface StudyCalendarRepository {

    void upsertAll(List<ChallengeLogDailyAggregateDto> list);

    List<DailyStudyDto> findDailyTotals(String userId, LocalDate from, LocalDate to);
}
