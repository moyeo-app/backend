package com.moyeo.backend.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "사용자 RESPONSE DTO")
public class UserResponseDto {
    @Schema(description = "사용자 ID", example = "2bc08f56-b63b-4860-b9eb-420bda33aa36")
    private String userId;
}
