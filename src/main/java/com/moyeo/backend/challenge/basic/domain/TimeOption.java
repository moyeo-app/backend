package com.moyeo.backend.challenge.basic.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "시간 인증제 옵션")
public class TimeOption implements ChallengeOption {
    @NotNull
    @Min(60)
    @Max(1440)
    private Integer time;
}
