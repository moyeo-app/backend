package com.moyeo.backend.study.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "일별 본인 공부 시간 READ RESPONSE DTO")
public record DailyStudyDto(
        @Schema(description = "날짜") LocalDate date,
        @Schema(description = "총 공부 시간 (분)") int totalMinutes)
{
    @QueryProjection
    public DailyStudyDto {

    }
}