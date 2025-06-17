package com.moyeo.backend.auth.application;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import com.moyeo.backend.auth.domain.Provider;
import com.moyeo.backend.auth.infrastructure.client.OAuthProviderService;
import com.moyeo.backend.auth.infrastructure.factory.OAuthProviderFactory;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private OAuthProviderFactory providerFactory;

    @Mock
    private OAuthProviderService providerService;

    @Mock
    private OauthRepository oauthRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("카카오 로그인 성공 테스트 - 신규 사용자")
    void loginWithKakao_성공_테스트_신규_사용자() {
        // given
        String provider = "kakao";
        String accessToken = "token";
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .accessToken(accessToken)
                .build();

        OAuthUserInfo userInfo = mock(OAuthUserInfo.class);

        when(providerFactory.getProvider(provider)).thenReturn(providerService);
        when(providerService.getUserInfo(accessToken)).thenReturn(userInfo);
        when(userInfo.getOauthId()).thenReturn("1");
        when(oauthRepository.findByOauthIdAndProvider("1", Provider.KAKAO))
                .thenReturn(Optional.empty());

        // when
        LoginResponseDto responseDto = authService.login(provider, requestDto);

        // then
        assertTrue(responseDto.isNewUser());
        assertEquals("1", responseDto.getOauthId());
    }

    @Test
    @DisplayName("카카오 로그인 성공 테스트 - 기존 사용자")
    void loginWithKakao_성공_테스트_기존_사용자() {
        // given
        String provider = "kakao";
        String accessToken = "token";
        String userId = "UUID-USER-1";
        String nickname = "코딩짱짱맨";
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .accessToken(accessToken)
                .build();

        User user = User.builder()
                .id(userId)
                .nickname(nickname)
                .build();

        Oauth oauth = Oauth.builder()
                .user(user)
                .oauthId("1")
                .provider(Provider.KAKAO)
                .build();

        OAuthUserInfo userInfo = mock(OAuthUserInfo.class);

        when(providerFactory.getProvider(provider)).thenReturn(providerService);
        when(providerService.getUserInfo(accessToken)).thenReturn(userInfo);
        when(userInfo.getOauthId()).thenReturn("1");
        when(oauthRepository.findByOauthIdAndProvider("1", Provider.KAKAO))
                .thenReturn(Optional.of(oauth));
        when(jwtUtil.createToken(userId, nickname))
                .thenReturn("jwtToken");

        // when
        LoginResponseDto responseDto = authService.login(provider, requestDto);

        // then
        assertFalse(responseDto.isNewUser());
        assertEquals("jwtToken", responseDto.getJwtAccessToken());
    }

    @Test
    @DisplayName("카카오 로그인 실패 테스트 - 만료 토큰")
    void loginWithKakao_실패_테스트_만료_토큰() {
        // given
        String provider = "kakao";
        String accessToken = "expired-token";
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .accessToken(accessToken)
                .build();

        when(providerFactory.getProvider(provider)).thenReturn(providerService);
        when(providerService.getUserInfo(accessToken))
                .thenThrow(new CustomException(ErrorCode.EXPIRED_OAUTH_TOKEN));

        // when & then
        CustomException ex = assertThrows(CustomException.class, () -> {
            authService.login(provider, requestDto);
        });
        assertEquals(ErrorCode.EXPIRED_OAUTH_TOKEN, ex.getResponseCode());
    }
}