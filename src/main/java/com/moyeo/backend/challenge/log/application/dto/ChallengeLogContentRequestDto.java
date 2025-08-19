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
@Schema(description = "챌린지 인증 내용 입력 REQUEST DTO")
public class ChallengeLogContentRequestDto {

    @Schema(description = "내용", example = "오늘은 BFS 관련 알고리즘을 2개 풀었다. BFS는 너비 우선 탐색으로, 상하좌우를 돌고, 스택에 넣고 방문을 확인하는 방식으로 이뤄진다. 틀이 어느정도 정해져 있어서, 이를 활용하면 좋을거같다.")
    @NotBlank
    private String text;
}
