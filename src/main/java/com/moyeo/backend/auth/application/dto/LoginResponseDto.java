package com.moyeo.backend.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "소셜 로그인 RESPONSE DTO")
public class LoginResponseDto {
    @Schema(description = "신규 사용자 유무 (true 면 회원 등록 필요)", example = "true")
    private boolean isNewUser;

    @Schema(description = "JWT Access Token (기존 사용자일 경우 응답)", example = "null")
    private String jwtAccessToken;

    @Schema(description = "소셜 로그인 고유 사용자 ID (신규 회원일 경우 응답)", example = "1122385832637846")
    private String oauthId;
}
