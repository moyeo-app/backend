package com.moyeo.backend.challenge.basic.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalTime;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "출석/내용 인증제 옵션")
public class StartEndOptionDto implements ChallengeOptionDto {
    @Schema(description = "시작 시간 (HH:mm)", example = "10:00")
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime start;

    @Schema(description = "종료 시간 (HH:mm)", example = "12:00")
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime end;
}