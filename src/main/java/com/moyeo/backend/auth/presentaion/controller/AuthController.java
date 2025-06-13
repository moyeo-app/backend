package com.moyeo.backend.auth.presentaion.controller;

import com.moyeo.backend.auth.application.AuthService;
import com.moyeo.backend.auth.presentaion.dtos.LoginRequestDto;
import com.moyeo.backend.auth.presentaion.dtos.LoginResponseDto;
import com.moyeo.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "AuthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<LoginResponseDto>> loginWithKakao(@Valid @RequestBody LoginRequestDto requestDto) {
        log.info("accessToken : {}", requestDto.getAccessToken());
        return ResponseEntity.ok().body(ApiResponse.success(authService.loginWithKakao(requestDto)));
    }
}
