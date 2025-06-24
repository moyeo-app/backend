package com.moyeo.backend.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "사용자 RESPONSE DTO")
public class UserResponseDto {
    private String userId;
}
