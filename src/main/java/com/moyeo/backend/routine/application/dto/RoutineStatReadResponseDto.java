package com.moyeo.backend.routine.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Builder
@Schema(description = "주간 학습 통계 READ RESPONSE DTO")
public record RoutineStatReadResponseDto(
        @Schema(description = "사용자 ID") String userId,
        @Schema(description = "주차 시작 날짜 (월요일)") LocalDate startDate,
        @Schema(description = "주간 총 공부 시간 (분)") int totalMinutes,
        @Schema(description = "하루 평균 공부 시간 (분)") int avgMinutes,
        @Schema(description = "주간 총 참여일") int activeDays,
        @Schema(description = "집중 요일") DayOfWeek focusDay,
        @Schema(description = "가장 적게 공부한 요일") DayOfWeek leastDay,
        @Schema(description = "출석률 높은 요일") List<DayOfWeek> highAttendanceDays
) {}