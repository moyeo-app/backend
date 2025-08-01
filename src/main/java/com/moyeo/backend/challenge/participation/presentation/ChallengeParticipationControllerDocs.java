package com.moyeo.backend.challenge.participation.presentation;


import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationResponseDto;
import com.moyeo.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "챌린지 참여 API Controller", description = "챌린지 참여 관련 API 목록입니다.")
@SecurityRequirement(name = "bearerAuth")
public interface ChallengeParticipationControllerDocs {

    @Operation(summary = "챌린지 참여 가능 여부 확인 API", description = "챌린지 참여 가능 여부 확인 API 입니다.")
    ResponseEntity<ApiResponse<Boolean>> check(String id);


    @Operation(summary = "챌린지 참여 API", description = "챌린지 참여 API 입니다.")
    ResponseEntity<ApiResponse<ChallengeParticipationResponseDto>> participate(String id, ChallengeParticipationRequestDto requestDto);
}
