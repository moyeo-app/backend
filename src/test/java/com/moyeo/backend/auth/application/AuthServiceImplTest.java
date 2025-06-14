package com.moyeo.backend.auth.application;

import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import com.moyeo.backend.auth.domain.Provider;
import com.moyeo.backend.auth.infrastructure.KakaoService;
import com.moyeo.backend.auth.infrastructure.KakaoUserInfoDto;
import com.moyeo.backend.auth.presentaion.dtos.LoginRequestDto;
import com.moyeo.backend.auth.presentaion.dtos.LoginResponseDto;
import com.moyeo.backend.auth.util.JwtUtil;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private KakaoService kakaoService;

    @Mock
    private OauthRepository oauthRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("카카오 로그인 성공 테스트 - 신규 사용자")
    void loginWithKakao_성공_테스트_신규_사용자() {
        // given
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .accessToken("token")
                .build();

        KakaoUserInfoDto kakaoUserInfoDto = KakaoUserInfoDto.builder()
                .id(1L)
                .build();

        when(kakaoService.getKakaoUserInfo("token")).thenReturn(kakaoUserInfoDto);
        when(oauthRepository.findByOauthIdAndProvider("1", Provider.KAKAO))
                .thenReturn(Optional.empty());

        // when
        LoginResponseDto responseDto = authService.loginWithKakao(requestDto);

        // then
        assertTrue(responseDto.isNewUser());
        assertEquals("1", responseDto.getOauthId());
    }

    @Test
    @DisplayName("카카오 로그인 성공 테스트 - 기존 사용자")
    void loginWithKakao_성공_테스트_기존_사용자() {
        // given
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .accessToken("token")
                .build();

        KakaoUserInfoDto kakaoUserInfoDto = KakaoUserInfoDto.builder()
                .id(1L)
                .build();

        User user = User.builder()
                .id("UUID-USER-1")
                .nickname("코딩짱짱맨")
                .build();

        Oauth oauth = Oauth.builder()
                .user(user)
                .oauthId("1")
                .provider(Provider.KAKAO)
                .build();

        when(kakaoService.getKakaoUserInfo("token")).thenReturn(kakaoUserInfoDto);
        when(oauthRepository.findByOauthIdAndProvider("1", Provider.KAKAO))
                .thenReturn(Optional.of(oauth));
        when(jwtUtil.createToken("UUID-USER-1", "코딩짱짱맨"))
                .thenReturn("jwtToken");

        // when
        LoginResponseDto responseDto = authService.loginWithKakao(requestDto);

        // then
        assertFalse(responseDto.isNewUser());
        assertEquals("jwtToken", responseDto.getJwtAccessToken());
    }

    @Test
    @DisplayName("카카오 로그인 실패 테스트 - 만료 토큰")
    void loginWithKakao_실패_테스트_만료_토큰() {
        // given
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .accessToken("expired-token")
                .build();

        when(kakaoService.getKakaoUserInfo("expired-token"))
                .thenThrow(new CustomException(ErrorCode.EXPIRED_KAKAO_TOKEN));

        // when & then
        CustomException ex = assertThrows(CustomException.class, () -> {
            authService.loginWithKakao(requestDto);
        });
        assertEquals(ErrorCode.EXPIRED_KAKAO_TOKEN, ex.getResponseCode());
    }
}