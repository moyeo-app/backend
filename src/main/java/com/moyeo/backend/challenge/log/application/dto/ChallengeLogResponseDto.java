package com.moyeo.backend.challenge.log.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "챌린지 인증 RESPONSE DTO")
public class ChallengeLogResponseDto {

    @Schema(description = "인증 ID", example = "ed8f52fe-f9c0-41db-9e52-57f25185c382")
    private String logId;
}
