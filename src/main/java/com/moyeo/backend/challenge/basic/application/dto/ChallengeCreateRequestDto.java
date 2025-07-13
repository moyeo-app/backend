package com.moyeo.backend.challenge.basic.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.moyeo.backend.challenge.basic.domain.StartEndOption;
import com.moyeo.backend.challenge.basic.domain.TimeOption;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeType;
import com.moyeo.backend.challenge.basic.infrastructure.validator.ValidChallengeOption;
import com.moyeo.backend.challenge.basic.presentation.OptionDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@ValidChallengeOption
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 생성 REQUEST DTO")
public class ChallengeCreateRequestDto {

    @Schema(description = "챌린지명", example = "매일 2시간 공부 인증")
    @NotBlank
    private String title;

    @Schema(description = "시작 날짜", example = "2025-07-20")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "종료 날짜", example = "2025-09-20")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "챌린지 인증 방식", example = "TIME")
    @NotNull
    private ChallengeType type;

    @Schema(description = "최대 참가 인원", example = "20")
    @NotNull
    @Min(1)
    @Max(20)
    private Integer maxParticipants;

    @Schema(description = "참가비", example = "5000")
    @NotNull
    @Min(5000)
    @Max(50000)
    private Integer fee;

    @Schema(description = "챌린지 상세 설명", example = "매일 2시간 이상 공부하는 챌린지입니다.")
    @NotBlank
    private String description;

    @Schema(
            description = "챌린지 유형에 따른 옵션",
            oneOf = {TimeOption.class, StartEndOption.class}
    )
    @NotNull
    @Valid
    @JsonDeserialize(using = OptionDeserializer.class)
    private ChallengeOptionDto option;

    @Schema(description = "규칙 (주 n 일)", example = "7")
    @NotNull
    @Min(1)
    @Max(7)
    private Integer rule;

    @Schema(description = "결제 ID", example = "ed8f52fe-f9c0-41db-9e52-57f25185c382")
    @NotBlank
    private String paymentId;
}
