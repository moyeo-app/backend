package com.moyeo.backend.auth.infrastructure.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenRequestDto {
    private String code;
}
