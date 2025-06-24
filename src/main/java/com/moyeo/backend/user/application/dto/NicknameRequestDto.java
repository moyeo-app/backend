package com.moyeo.backend.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "닉네임 유효성 검사 REQUEST DTO")
public class NicknameRequestDto {
    private String nickname;
}
