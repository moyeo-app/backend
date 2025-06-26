package com.moyeo.backend.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "소셜 로그인 REQUEST DTO")
public class LoginRequestDto {
    @Schema(description = "access_token", example = "ZRffx-7GXfxBUefZMnK_icHtxJQXR6rNAAAAAQoNIdkAAAGXq6clFm1lzvpaqIEo")
    @NotBlank
    private String accessToken;
}
