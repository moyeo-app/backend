package com.moyeo.backend.user.application.service;

import com.moyeo.backend.user.application.dto.RegisterRequestDto;
import com.moyeo.backend.user.application.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(RegisterRequestDto requestDto);

    void validNickname(String nickname);
}
