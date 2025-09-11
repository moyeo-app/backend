package com.moyeo.backend.study.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "일별 본인 공부 시간 목록 READ RESPONSE DTO")
public class StudyCalendarReadResponseDto {
    @Schema(description = "일별 본인 공부 시간 (분)")
    private List<DailyStudyDto> days;
}