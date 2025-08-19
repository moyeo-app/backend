package com.moyeo.backend.challenge.log.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 인증 키워드 입력 REQUEST DTO")
public class ChallengeLogKeywordRequestDto {

    @Schema(description = "키워드 1", example = "알고리즘")
    @NotBlank
    private String keyword1;

    @Schema(description = "키워드 2", example = "BFS")
    @NotBlank
    private String keyword2;

    @Schema(description = "키워드 3", example = "상하좌우")
    @NotBlank
    private String keyword3;

}
