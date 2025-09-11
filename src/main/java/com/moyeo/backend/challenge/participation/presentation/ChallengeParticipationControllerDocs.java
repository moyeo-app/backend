package com.moyeo.backend.challenge.participation.presentation;


import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadResponseDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;
import com.moyeo.backend.common.request.PageRequestDto;
import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@Tag(name = "챌린지 참여 API Controller", description = "챌린지 참여 관련 API 목록입니다.")
@SecurityRequirement(name = "bearerAuth")
public interface ChallengeParticipationControllerDocs {

    @Operation(summary = "챌린지 참여 가능 여부 확인 API", description = "챌린지 참여 가능 여부 확인 API 입니다.")
    ResponseEntity<ApiResponse<Boolean>> check(String challengeId);

    @Operation(summary = "챌린지 참여 API", description = "챌린지 참여 API 입니다.")
    ResponseEntity<ApiResponse<Void>> participate(String challengeId, ChallengeParticipationRequestDto requestDto);

    @Operation(summary = "챌린지 참여 목록 조회 API", description = "챌린지 목록 조회 API 입니다.")
    ResponseEntity<ApiResponse<PageResponse<ChallengeParticipationReadResponseDto>>> gets(
            ChallengeParticipationReadRequestDto requestDto, PageRequestDto page);

    @Operation(summary = "[ADMIN] 챌린지 참여 상태 변경 API", description = "[ADMIN] 챌린지 참여 상태 변경 API 입니다.")
    ResponseEntity<ApiResponse<Void>> updateStatus(LocalDate from, LocalDate to);
}
