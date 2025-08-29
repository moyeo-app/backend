package com.moyeo.backend.routine.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Schema(description = "사용자 루틴 분석")
    private String routineAnalysis;

    @Schema(description = "감성 피드백")
    private String emotionalFeedback;

    @Schema(description = "다음 주 학습 루틴")
    private String nextWeekRoutine;
}
