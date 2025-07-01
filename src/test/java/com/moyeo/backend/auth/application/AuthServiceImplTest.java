package com.moyeo.backend.auth.application;

import com.moyeo.backend.auth.application.dto.LoginRequestDto;
import com.moyeo.backend.auth.application.dto.LoginResponseDto;
import com.moyeo.backend.auth.application.dto.TokenRequestDto;
import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import com.moyeo.backend.auth.domain.Provider;
import com.moyeo.backend.auth.infrastructure.client.OAuthProviderService;
import com.moyeo.backend.auth.infrastructure.dtos.TokenResponse;
import com.moyeo.backend.auth.infrastructure.factory.OAuthProviderFactory;
import com.moyeo.backend.auth.util.JwtUtil;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
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

    @BeforeEach
    void setUp() {
        when(providerFactory.getProvider(anyString())).thenReturn(providerService);
    }

    private OAuthUserInfo mockUserInfo(String oauthId) {
        OAuthUserInfo userInfo = mock(OAuthUserInfo.class);
        when(userInfo.getOauthId()).thenReturn(oauthId);
        return userInfo;
    }

    private User createUser(String id, String nickname) {
        return User.builder()
                .id(id)
                .nickname(nickname)
                .build();
    }

    private Oauth createOauth(String oauthId, Provider provider, User user) {
        return Oauth.builder()
                .user(user)
                .oauthId(oauthId)
                .provider(provider)
                .build();
    }

    private void assertNewUser(LoginResponseDto dto, String expectedOauthId) {
        assertTrue(dto.isNewUser());
        assertEquals(expectedOauthId, dto.getOauthId());
    }

    private void assertExistingUser(LoginResponseDto dto, String expectedToken) {
        assertFalse(dto.isNewUser());
        assertEquals(expectedToken, dto.getJwtAccessToken());
    }

    @Nested
    @DisplayName("accessToken 전달 버전 로그인 테스트")
    class LoginTests {

        @ParameterizedTest(name = "신규 사용자 성공 테스트 {0}")
        @CsvSource({"kakao, KAKAO", "google, GOOGLE"})
        void login_신규_사용자(String provider, Provider enumProvider) {
            // given
            String oauthId = "UUID-OAUTH-1";
            String accessToken = "token";
            LoginRequestDto requestDto = LoginRequestDto.builder()
                    .accessToken(accessToken)
                    .build();

            OAuthUserInfo userInfo = mockUserInfo(oauthId);
            when(providerService.getUserInfo(accessToken)).thenReturn(userInfo);
            when(oauthRepository.findByOauthIdAndProvider(oauthId, enumProvider))
                    .thenReturn(Optional.empty());

            // when
            LoginResponseDto responseDto = authService.login(provider, requestDto);

            // then
            assertNewUser(responseDto, oauthId);
        }

        @ParameterizedTest(name = "기존 사용자 성공 테스트 {0}")
        @CsvSource({"kakao, KAKAO", "google, GOOGLE"})
        void login_기존_사용자(String provider, Provider enumProvider) {
            // given
            String oauthId = "UUID-OAUTH-1";
            String accessToken = "token";
            String userId = "UUID-USER-1";
            String nickname = "코딩짱짱맨";
            String jwtToken = "jwtToken";
            LoginRequestDto requestDto = LoginRequestDto.builder()
                    .accessToken(accessToken)
                    .build();
            User user = createUser(userId, nickname);
            Oauth oauth = createOauth(oauthId, enumProvider, user);

            OAuthUserInfo userInfo = mockUserInfo(oauthId);
            when(providerService.getUserInfo(accessToken)).thenReturn(userInfo);
            when(oauthRepository.findByOauthIdAndProvider(oauthId, enumProvider))
                    .thenReturn(Optional.of(oauth));
            when(jwtUtil.createToken(userId, nickname))
                    .thenReturn(jwtToken);

            // when
            LoginResponseDto responseDto = authService.login(provider, requestDto);

            // then
            assertExistingUser(responseDto, jwtToken);
        }

        @ParameterizedTest(name = "실패 테스트 - 유효하지 않은 토큰 {0}")
        @CsvSource({"kakao", "google"})
        void login_만료_토큰(String provider) {
            // given
            String accessToken = "expired-token";
            LoginRequestDto requestDto = LoginRequestDto.builder()
                    .accessToken(accessToken)
                    .build();

            when(providerService.getUserInfo(accessToken))
                    .thenThrow(new CustomException(ErrorCode.INVALID_OAUTH_TOKEN));

            // when & then
            CustomException ex = assertThrows(CustomException.class, () -> {
                authService.login(provider, requestDto);
            });
            assertEquals(ErrorCode.INVALID_OAUTH_TOKEN, ex.getResponseCode());
        }
    }

    @Nested
    @DisplayName("code 전달 버전 로그인 테스트")
    class CallbackTests {

        @ParameterizedTest(name = "신규 사용자 성공 테스트 {0}")
        @CsvSource({"kakao, KAKAO", "google, GOOGLE"})
        void callback_신규_사용자(String provider, Provider enumProvider) {
            // given
            String oauthId = "UUID-OAUTH-1";
            String code = "code";
            String accessToken = "token";

            TokenRequestDto requestDto = TokenRequestDto.builder()
                    .code(code)
                    .build();

            TokenResponse tokenResponse = TokenResponse.builder()
                    .accessToken(accessToken)
                    .build();

            OAuthUserInfo userInfo = mockUserInfo(oauthId);
            when(providerService.getAccessToken(code)).thenReturn(tokenResponse);
            when(providerService.getUserInfo(tokenResponse.getAccessToken())).thenReturn(userInfo);

            when(oauthRepository.findByOauthIdAndProvider(oauthId, enumProvider))
                    .thenReturn(Optional.empty());

            // when
            LoginResponseDto responseDto = authService.callback(provider, requestDto);

            // then
            assertNewUser(responseDto, oauthId);
        }

        @ParameterizedTest(name = "기존 사용자 성공 테스트 {0}")
        @CsvSource({"kakao, KAKAO", "google, GOOGLE"})
        void callback_기존_사용자(String provider, Provider enumProvider) {
            // given
            String oauthId = "UUID-OAUTH-1";
            String code = "code";
            String accessToken = "token";
            String userId = "UUID-USER-1";
            String nickname = "코딩짱짱맨";
            String jwtToken = "jwtToken";

            TokenRequestDto requestDto = TokenRequestDto.builder()
                    .code(code)
                    .build();

            TokenResponse tokenResponse = TokenResponse.builder()
                    .accessToken(accessToken)
                    .build();

            User user = createUser(userId, nickname);
            Oauth oauth = createOauth(oauthId, enumProvider, user);

            OAuthUserInfo userInfo = mockUserInfo(oauthId);
            when(providerService.getAccessToken(code)).thenReturn(tokenResponse);
            when(providerService.getUserInfo(tokenResponse.getAccessToken())).thenReturn(userInfo);
            when(oauthRepository.findByOauthIdAndProvider(oauthId, enumProvider))
                    .thenReturn(Optional.of(oauth));
            when(jwtUtil.createToken(userId, nickname))
                    .thenReturn(jwtToken);

            // when
            LoginResponseDto responseDto = authService.callback(provider, requestDto);

            // then
            assertExistingUser(responseDto, jwtToken);
        }

        @ParameterizedTest(name = "실패 테스트 - 유효하지 않은 코드 {0}")
        @CsvSource({"kakao", "google"})
        void callback_유효하지_않은_코드(String provider) {
            // given
            String code = "invalid_code";
            TokenRequestDto requestDto = TokenRequestDto.builder()
                    .code(code)
                    .build();

            when(providerService.getAccessToken(code))
                    .thenThrow(new CustomException(ErrorCode.OAUTH_API_CALL_FAILED));

            // when & then
            CustomException ex = assertThrows(CustomException.class, () -> {
                authService.callback(provider, requestDto);
            });
            assertEquals(ErrorCode.OAUTH_API_CALL_FAILED, ex.getResponseCode());
        }


        @ParameterizedTest(name = "실패 테스트 - 유효하지 않은 토큰 {0}")
        @CsvSource({"kakao", "google"})
        void login_만료_토큰(String provider) {
            // given
            String accessToken = "expired-token";
            LoginRequestDto requestDto = LoginRequestDto.builder()
                    .accessToken(accessToken)
                    .build();

            when(providerService.getUserInfo(accessToken))
                    .thenThrow(new CustomException(ErrorCode.INVALID_OAUTH_TOKEN));

            // when & then
            CustomException ex = assertThrows(CustomException.class, () -> {
                authService.login(provider, requestDto);
            });
            assertEquals(ErrorCode.INVALID_OAUTH_TOKEN, ex.getResponseCode());
        }
    }
}