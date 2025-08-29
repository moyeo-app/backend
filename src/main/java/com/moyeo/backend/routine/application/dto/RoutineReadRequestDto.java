package com.moyeo.backend.routine.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주간 학습 통계/루틴 리포트 REQUEST PARAMETER DTO")
public class RoutineReadRequestDto {

    @Schema(description = "조회 날짜(yyyy-MM-dd)", example = "2025-08-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
