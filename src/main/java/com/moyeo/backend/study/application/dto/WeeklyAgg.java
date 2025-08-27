package com.moyeo.backend.study.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record WeeklyAgg(
        String userId,
        LocalDate startDate,
        int mon, int tue, int wed, int thu, int fri, int sat, int sun,
        int totalMinutes,
        int avgMinutes
) {
    @QueryProjection
    public WeeklyAgg {}
}