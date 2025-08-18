package com.moyeo.backend.challenge.log.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("CONTENT")
public class ContentLog extends ChallengeLogContent {
    private List<@NotBlank String> keywords;
    private String text;
}
