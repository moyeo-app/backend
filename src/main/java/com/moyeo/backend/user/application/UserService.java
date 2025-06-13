package com.moyeo.backend.user.application;

import com.moyeo.backend.user.presentaion.dtos.RegisterRequestDto;
import com.moyeo.backend.user.presentaion.dtos.UserResponseDto;

public interface UserService {
    UserResponseDto register(RegisterRequestDto requestDto);
}
