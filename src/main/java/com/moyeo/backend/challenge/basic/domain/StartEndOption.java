package com.moyeo.backend.challenge.basic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartEndOption extends ChallengeOption {
    private final String type = "START_END";

    @JsonFormat(pattern = "HH:mm")
    private LocalTime start;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime end;

}