package com.moyeo.backend.auth.presentaion.controller;

import com.moyeo.backend.auth.application.dto.LoginRequestDto;
import com.moyeo.backend.auth.application.dto.LoginResponseDto;
import com.moyeo.backend.auth.application.dto.TokenRequestDto;
import com.moyeo.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "소셜 로그인 API Controller", description = "소셜 로그인 API 목록입니다.")
public interface AuthControllerDocs {

    @Operation(summary = "소셜 로그인 API (token 버전)", description = "소셜 로그인 API (token 버전) 입니다.")
    ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Parameter(description = "소셜 로그인 종류 (kakao, google)", example = "kakao")
            String provider, LoginRequestDto requestDto);

    @Operation(summary = "소셜 로그인 API (code 버전)", description = "소셜 로그인 API (code 버전) 입니다.")
    ResponseEntity<ApiResponse<LoginResponseDto>> callback(
            @Parameter(description = "소셜 로그인 종류 (kakao, google)", example = "kakao")
            String provider, TokenRequestDto requestDto);
}
