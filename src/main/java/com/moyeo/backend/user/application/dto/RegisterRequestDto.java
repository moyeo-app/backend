package com.moyeo.backend.user.application.dto;

import com.moyeo.backend.auth.domain.Provider;
import com.moyeo.backend.user.domain.Character;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "사용자 등록 REQUEST DTO")
@Builder
public class RegisterRequestDto {
    @NotNull
    private Provider provider;

    @NotBlank
    private String oauthId;

    @NotBlank
    private String nickname;

    @NotNull
    private Character character;
}
