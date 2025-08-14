package com.moyeo.backend.challenge.participation.application.dto;

import com.moyeo.backend.challenge.basic.domain.enums.ChallengeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 참여 목록 REQUEST PARAMETER DTO")
public class ChallengeParticipationReadRequestDto {

    @Schema(description = "날짜", example = "2025-08-14")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @Schema(description = "챌린지 상태", example = "INPROGRESS")
    private ChallengeStatus status;
}
