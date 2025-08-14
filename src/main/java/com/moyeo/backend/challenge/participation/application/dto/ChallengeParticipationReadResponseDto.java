package com.moyeo.backend.challenge.participation.application.dto;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import com.moyeo.backend.challenge.participation.domain.enums.ParticipationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "챌린지 참여 READ RESPONSE DTO")
public class ChallengeParticipationReadResponseDto {

    @Schema(description = "챌린지 정보")
    private ChallengeReadResponseDto challenge;

    @Schema(description = "챌린지 참여 상태", example = "INPROGRESS")
    private ParticipationStatus participationStatus;
}
