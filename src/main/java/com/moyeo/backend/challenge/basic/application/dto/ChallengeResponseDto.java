package com.moyeo.backend.challenge.basic.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "챌린지 기본 RESPONSE DTO")
public class ChallengeResponseDto {

    @Schema(description = "챌린지 ID", example = "ed8f52fe-f9c0-41db-9e52-57f25185c382")
    private String challengeId;
}
