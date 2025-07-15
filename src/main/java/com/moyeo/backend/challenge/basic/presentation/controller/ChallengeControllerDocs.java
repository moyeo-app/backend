package com.moyeo.backend.challenge.basic.presentation.controller;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeResponseDto;
import com.moyeo.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "챌린지 기본 API Controller", description = "챌린지 기본 정보 관련 API 목록입니다.")
public interface ChallengeControllerDocs {

    @Operation(summary = "챌린지 생성 API", description = "챌린지 생성 API 입니다.")
    ResponseEntity<ApiResponse<ChallengeResponseDto>> create(ChallengeCreateRequestDto requestDto);

    @Operation(summary = "챌린지 단건 조회 API", description = "챌린지 단건 조회 API 입니다.")
    ResponseEntity<ApiResponse<ChallengeReadResponseDto>> getById(String id);
}
