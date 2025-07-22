package com.moyeo.backend.challenge.basic.application.dto;

import com.moyeo.backend.challenge.basic.domain.enums.ChallengeStatus;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 목록 REQUEST PARAMETER DTO")
public class ChallengeReadRequestDto {

    @Schema(description = "챌린지명", example = "매일 2시간 공부 인증")
    private String title;

    @Schema(description = "챌린지 인증 유형", example = "TIME")
    private ChallengeType type;

    @Schema(description = "챌린지 상태", example = "RECRUITING")
    private ChallengeStatus status;
}
