package com.moyeo.backend.auth.presentaion.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "소셜 로그인 RESPONSE DTO")
public class LoginResponseDto {
    private boolean isNewUser;
    private String jwtAccessToken;
    private String oauthId;
}
