package com.moyeo.backend.user.application;

import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import com.moyeo.backend.auth.domain.Provider;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.user.domain.Character;
import com.moyeo.backend.user.domain.User;
import com.moyeo.backend.user.domain.UserRepository;
import com.moyeo.backend.user.presentaion.dtos.RegisterRequestDto;
import com.moyeo.backend.user.presentaion.dtos.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OauthRepository oauthRepository;

    @Test
    @DisplayName("사용자 등록 API 성공 테스트")
    void register_성공_테스트() {
        // given
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                .provider(Provider.KAKAO)
                .oauthId("1234567890")
                .nickname("코딩짱짱맨")
                .character(Character.BEAR)
                .build();
        when(userRepository.findByNickname("코딩짱짱맨")).thenReturn(Optional.empty());

        // when
        UserResponseDto responseDto = userService.register(requestDto);

        // then
        verify(userRepository).save(any(User.class));
        verify(oauthRepository).save(any(Oauth.class));
        assertNotNull(responseDto);
    }

    @Test
    @DisplayName("사용자 등록 API 실패 테스트 - 닉네임 중복")
    void register_닉네임_중복() {
        // given: 테스트 데이터
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                .provider(Provider.KAKAO)
                .oauthId("1234567890")
                .nickname("코딩짱짱맨")
                .character(Character.BEAR)
                .build();
        when(userRepository.findByNickname("코딩짱짱맨")).thenReturn(Optional.of(User.builder().nickname("코딩짱짱맨").build()));

        // when & then
        CustomException ex = assertThrows(CustomException.class, () -> {
            userService.register(requestDto);
        });
        assertEquals(ErrorCode.NICKNAME_ALREADY_EXISTS, ex.getResponseCode());
    }
}