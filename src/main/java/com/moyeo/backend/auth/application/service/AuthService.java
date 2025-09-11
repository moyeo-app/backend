package com.moyeo.backend.auth.application.service;

import com.moyeo.backend.auth.application.dto.TokenRequestDto;
import com.moyeo.backend.auth.application.dto.LoginRequestDto;
import com.moyeo.backend.auth.application.dto.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(String provider, LoginRequestDto dto);

    LoginResponseDto callback(String provider, TokenRequestDto dto);
}
