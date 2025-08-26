package com.moyeo.backend.challenge.log.application.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;

public record ChallengeLogDailyAggregateDto(
        String userId,
        LocalDate date,
        long totalMinutes)
{
    @QueryProjection
    public ChallengeLogDailyAggregateDto {
    }
}