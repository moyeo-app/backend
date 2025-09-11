package com.moyeo.backend.challenge.log.application.dto;

import com.moyeo.backend.challenge.log.domain.ChallengeLogStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 인증 REQUEST PARAMETER DTO")
public class ChallengeLogReadRequestDto {

    @Schema(description = "챌린지 인증 상태", example = "SUCCESS")
    private ChallengeLogStatus status;

}
