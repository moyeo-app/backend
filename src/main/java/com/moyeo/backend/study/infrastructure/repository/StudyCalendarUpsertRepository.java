package com.moyeo.backend.study.infrastructure.repository;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogDailyAggregateDto;

import java.util.List;

public interface StudyCalendarUpsertRepository {

    void upsertAll(List<ChallengeLogDailyAggregateDto> list);
}
