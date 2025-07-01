package com.moyeo.backend.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenRequestDto {
    @Schema(description = "승인 코드", example = "4/P7q7W91a-oMsCeLvIaQm6bTrgtp7")
    @NotBlank
    private String code;
}
