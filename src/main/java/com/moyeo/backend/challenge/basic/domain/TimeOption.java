package com.moyeo.backend.challenge.basic.domain;

import lombok.*;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeOption extends ChallengeOption {
    private final String type = "TIME";
    private Integer time;
}
