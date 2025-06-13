package com.moyeo.backend.auth.application;

import com.moyeo.backend.auth.presentaion.dtos.LoginRequestDto;
import com.moyeo.backend.auth.presentaion.dtos.LoginResponseDto;

public interface AuthService {

    LoginResponseDto loginWithKakao(LoginRequestDto dto);
}
