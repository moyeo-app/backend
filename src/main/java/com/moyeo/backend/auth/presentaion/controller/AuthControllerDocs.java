package com.moyeo.backend.auth.presentaion.controller;

import com.moyeo.backend.auth.presentaion.dtos.LoginRequestDto;
import com.moyeo.backend.auth.presentaion.dtos.LoginResponseDto;
import com.moyeo.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "소셜 로그인 API Controller", description = "소셜 로그인 API 목록입니다.")
public interface AuthControllerDocs {

    @Operation(summary = "카카오 소셜 로그인 API", description = "카카오 소셜 로그인 API 입니다.")
    ResponseEntity<ApiResponse<LoginResponseDto>> loginWithKakao(LoginRequestDto requestDto);
}
