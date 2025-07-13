package com.moyeo.backend.challenge.basic.domain;

import lombok.*;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartEndOption extends ChallengeOption {
    private final String type = "START_END";
    private String start;
    private String end;

}