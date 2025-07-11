package com.moyeo.backend.challenge.basic.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "출석/내용 인증제 옵션")
public class StartEndOption implements ChallengeOption {
    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시간은 HH:mm 형식이어야 합니다.")
    private String start;

    @NotBlank
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시간은 HH:mm 형식이어야 합니다.")
    private String end;

}