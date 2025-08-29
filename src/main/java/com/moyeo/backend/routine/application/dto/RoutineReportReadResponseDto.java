package com.moyeo.backend.routine.application.dto;

import com.moyeo.backend.routine.domain.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "루틴 리포트 조회 READ RESPONSE DTO")
public record RoutineReportReadResponseDto(
        @Schema(description = "사용자 ID") String userId,
        @Schema(description = "주차 시작 날짜 (월요일)") LocalDate startDate,
        @Schema(description = "사용자 루틴 분석 리포트") Report report
) {}
