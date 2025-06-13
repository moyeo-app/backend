package com.moyeo.backend.auth.presentaion.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "소셜 로그인 REQUEST DTO")
public class LoginRequestDto {
    @NotBlank
    private String accessToken;
}
