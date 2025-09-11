package com.moyeo.backend.challenge.basic.application.dto;

import com.moyeo.backend.challenge.basic.domain.enums.ChallengeStatus;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@Schema(description = "챌린지 기본 READ RESPONSE DTO")
public class ChallengeReadResponseDto {

    @Schema(description = "챌린지 ID", example = "ed8f52fe-f9c0-41db-9e52-57f25185c382")
    private String challengeId;

    @Schema(description = "챌린지명", example = "매일 2시간 공부 인증")
    private String title;

    @Schema(description = "시작 날짜", example = "2025-07-20")
    private LocalDate startDate;

    @Schema(description = "종료 날짜", example = "2025-09-20")
    private LocalDate endDate;

    @Schema(description = "챌린지 인증 유형", example = "TIME")
    private ChallengeType type;

    @Schema(description = "최대 참가 인원", example = "20")
    private int maxParticipants;

    @Schema(description = "현재 참가 인원", example = "1")
    private int participantsCount;

    @Schema(description = "참가비", example = "5000")
    private int fee;

    @Schema(description = "챌린지 상세 설명", example = "매일 2시간 이상 공부하는 챌린지입니다.")
    private String description;

    @Schema(description = "챌린지 상태", example = "RECRUITING")
    private ChallengeStatus status;

    @Schema(
            description = "챌린지 유형에 따른 옵션",
            oneOf = {TimeOptionDto.class, StartEndOptionDto.class}
    )
    private ChallengeOptionDto option;

    @Schema(description = "규칙 (주 n 일)", example = "7")
    private int rule;

    @Schema(description = "챌린지 참여 여부", example = "true")
    private Boolean participating;
}
