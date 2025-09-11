package com.moyeo.backend.challenge.log.presentation;

import com.moyeo.backend.challenge.log.application.dto.*;
import com.moyeo.backend.common.request.PageRequestDto;
import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "챌린지 인증 API Controller", description = "챌린지 인증 관련 API 목록입니다.")
@SecurityRequirement(name = "bearerAuth")
public interface ChallengeLogControllerDocs {

    @Operation(summary = "챌린지 인증 키워드 입력 API", description = "챌린지 인증 키워드 입력 API 입니다.")
    ResponseEntity<ApiResponse<ChallengeLogResponseDto>> create(String challengeId, ChallengeLogKeywordRequestDto requestDto);

    @Operation(summary = "챌린지 인증 내용 입력 API", description = "챌린지 인증 내용 입력 API 입니다.")
    ResponseEntity<ApiResponse<ChallengeLogResponseDto>> update(String challengeId,
                                                                String logId,
                                                                ChallengeLogContentRequestDto requestDto);

    @Operation(summary = "챌린지 인증 내용 목록 조회 API", description = "챌린지 인증 내용 목록 조회 API 입니다.")
    ResponseEntity<ApiResponse<PageResponse<ChallengeLogReadResponseDto>>> gets(String challengeId,
                                                                 ChallengeLogReadRequestDto requestDto,
                                                                 PageRequestDto page);

    @Operation(summary = "본인 챌린지 인증 내용 단건 조회 API", description = "본인 챌린지 인증 내용 단건 조회 API 입니다.")
    ResponseEntity<ApiResponse<ChallengeLogReadResponseDto>> getByUser(String challengeId);
}
