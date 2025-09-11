package com.moyeo.backend.auth.infrastructure.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleResponseDto {
    private String sub;
}
