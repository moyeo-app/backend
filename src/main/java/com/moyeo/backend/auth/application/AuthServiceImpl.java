package com.moyeo.backend.auth.application;

import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import com.moyeo.backend.auth.domain.Provider;
import com.moyeo.backend.auth.infrastructure.KakaoService;
import com.moyeo.backend.auth.infrastructure.KakaoUserInfoDto;
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

    private final KakaoService kakaoService;
    private final OauthRepository oauthRepository;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponseDto loginWithKakao(LoginRequestDto dto) {
        String accessToken = dto.getAccessToken();

        KakaoUserInfoDto kakaoUserInfoDto = kakaoService.getKakaoUserInfo(accessToken);
        String oauthId = String.valueOf(kakaoUserInfoDto.getId());
        log.info("oauthId: {}", oauthId);

        return isNewUser(oauthId, Provider.KAKAO);
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
