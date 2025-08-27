package com.moyeo.backend.routine.presentation;

import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.routine.application.dto.RoutineStatReadRequestDto;
import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "학습 통계 관련 API Controller", description = "학습 통계 관련 API 목록입니다.")
@SecurityRequirement(name = "bearerAuth")
public interface RoutineControllerDocs {

    @Operation(summary = "본인 주간 학습 통계 조회 API", description = "본인 주간 학습 통계 조회 API 입니다.")
    ResponseEntity<ApiResponse<RoutineStatReadResponseDto>> getRoutineStat(RoutineStatReadRequestDto requestDto);

}
