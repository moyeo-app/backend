package com.moyeo.backend.auth.application;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import com.moyeo.backend.auth.domain.Provider;
import com.moyeo.backend.auth.infrastructure.client.OAuthProviderService;
import com.moyeo.backend.auth.infrastructure.dtos.TokenRequestDto;
import com.moyeo.backend.auth.infrastructure.dtos.TokenResponse;
import com.moyeo.backend.auth.infrastructure.factory.OAuthProviderFactory;
import com.moyeo.backend.auth.presentaion.dtos.LoginRequestDto;
import com.moyeo.backend.auth.presentaion.dtos.LoginResponseDto;
import com.moyeo.backend.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j(topic = "AuthService")
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final OAuthProviderFactory providerFactory;
    private final OauthRepository oauthRepository;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponseDto login(String provider, LoginRequestDto dto) {
        OAuthProviderService providerService = providerFactory.getProvider(provider);
        OAuthUserInfo userInfo = providerService.getUserInfo(dto.getAccessToken());
        String oauthId = userInfo.getOauthId();

        log.info("provider: {} oauthId: {}", provider, oauthId);

        return isNewUser(oauthId, Provider.valueOf(provider.toUpperCase()));
    }

    @Override
    public LoginResponseDto callback(String provider, TokenRequestDto dto) {
        OAuthProviderService providerService = providerFactory.getProvider(provider);
        TokenResponse kakaoTokenResponse = providerService.getAccessToken(dto.getCode());
        OAuthUserInfo userInfo = providerService.getUserInfo(kakaoTokenResponse.getAccessToken());
        String oauthId = userInfo.getOauthId();

        log.info("provider: {} code : {} oauthId: {}", provider, dto.getCode(), oauthId);

        return isNewUser(oauthId, Provider.valueOf(provider.toUpperCase()));
    }

    private LoginResponseDto isNewUser(String oauthId, Provider provider) {
        Optional<Oauth> oauth = oauthRepository.findByOauthIdAndProvider(oauthId, provider);

        if (!oauth.isPresent()) {
            return LoginResponseDto.builder()
                    .isNewUser(true)
                    .oauthId(oauthId)
                    .build();
        };

        return LoginResponseDto.builder()
                .isNewUser(false)
                .jwtAccessToken(jwtUtil.createToken(oauth.get().getUser().getId(), oauth.get().getUser().getNickname()))
                .build();
    }
}
