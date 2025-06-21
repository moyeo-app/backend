package com.moyeo.backend.auth.infrastructure.client;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.infrastructure.adapter.GoogleUserInfo;
import com.moyeo.backend.auth.infrastructure.dtos.GoogleResponseDto;
import com.moyeo.backend.auth.infrastructure.dtos.TokenResponse;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j(topic = "GoogleAuthService")
@Service("google")
public class GoogleAuthService extends AbstractOAuthProviderService{
    private static final String API_BASE_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = settingHeader(accessToken);

        try {
            ResponseEntity<GoogleResponseDto> response = requestUserInfo(API_BASE_URL, headers, GoogleResponseDto.class);

            return GoogleUserInfo.builder()
                    .response(response.getBody())
                    .build();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                log.error("만료된 구글 토큰 사용 : {}", e.getMessage());
                throw new CustomException(ErrorCode.EXPIRED_OAUTH_TOKEN);
            }
            log.error("구글 API 요청 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.OAUTH_API_CALL_FAILED);
        }
    }

    @Override
    public TokenResponse getAccessToken(String authorizationCode) {
        return null;
    }
}
