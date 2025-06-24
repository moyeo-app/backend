package com.moyeo.backend.user.presentaion;

import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.user.application.service.UserService;
import com.moyeo.backend.user.application.dto.NicknameRequestDto;
import com.moyeo.backend.user.application.dto.RegisterRequestDto;
import com.moyeo.backend.user.application.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "UserController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController implements UserControllerDocs{

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        log.info("oauthId : {}", requestDto.getOauthId());
        return ResponseEntity.ok().body(ApiResponse.success(userService.register(requestDto)));
    }

    @PostMapping("/valid/nickname")
    public ResponseEntity<ApiResponse<Void>> validNickname(@Valid @RequestBody NicknameRequestDto requestDto) {
        log.info("nickname : {}", requestDto.getNickname());
        userService.validNickname(requestDto.getNickname());
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
