package com.moyeo.backend.study.application.dto;

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
@Schema(description = "일별 본인 공부 시간 목록 REQUEST PARAMETER DTO")
public class StudyCalendarReadRequestDto {

    @Schema(description = "조회 시작일(yyyy-MM-dd)", example = "2025-08-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @Schema(description = "조회 종료일(yyyy-MM-dd)", example = "2025-08-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
}
