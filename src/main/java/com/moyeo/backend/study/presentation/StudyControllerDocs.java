package com.moyeo.backend.study.presentation;

import com.moyeo.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@Tag(name = "스터디 관련 API Controller", description = "스터디 관련 API 목록입니다.")
@SecurityRequirement(name = "bearerAuth")
public interface StudyControllerDocs {

    @Operation(summary = "[ADMIN] 총 공부 시간 집계 API", description = "[ADMIN] 총 공부 시간 집계 API 입니다.")
    ResponseEntity<ApiResponse<Void>> aggreate(LocalDate from, LocalDate to);
}
