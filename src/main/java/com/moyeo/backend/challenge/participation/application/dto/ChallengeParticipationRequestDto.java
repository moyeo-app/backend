package com.moyeo.backend.challenge.participation.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 참여 REQUEST DTO")
public class ChallengeParticipationRequestDto {

    @Schema(description = "결제 ID", example = "ed8f52fe-f9c0-41db-9e52-57f25185c382")
    @NotBlank
    private String paymentId;
}
