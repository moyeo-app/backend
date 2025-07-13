package com.moyeo.backend.challenge.basic.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "출석/내용 인증제 옵션")
public class StartEndOptionDto implements ChallengeOptionDto {
    @Schema(description = "시작 시간 (HH:mm)", example = "10:00")
    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시간은 HH:mm 형식이어야 합니다.")
    private String start;

    @Schema(description = "종료 시간 (HH:mm)", example = "12:00")
    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시간은 HH:mm 형식이어야 합니다.")
    private String end;

}