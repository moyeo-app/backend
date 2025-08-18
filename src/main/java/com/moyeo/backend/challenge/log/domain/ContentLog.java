package com.moyeo.backend.challenge.log.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContentLog extends ChallengeLogContent {
    private final String type = "CONTENT";
    private List<@NotBlank String> keywords;
    private String text;
}
