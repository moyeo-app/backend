package com.moyeo.backend.user.presentaion;

import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.user.presentaion.dtos.RegisterRequestDto;
import com.moyeo.backend.user.presentaion.dtos.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "사용자 관리 API Controller", description = "사용자 관리 API 목록입니다.")
public interface UserControllerDocs {

    @Operation(summary = "사용자 등록 API", description = "사용자 등록 API 입니다.")
    ResponseEntity<ApiResponse<UserResponseDto>> register(RegisterRequestDto requestDto);
}
