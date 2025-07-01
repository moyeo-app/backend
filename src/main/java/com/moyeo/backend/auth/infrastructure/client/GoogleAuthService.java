package com.moyeo.backend.auth.infrastructure.client;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.infrastructure.adapter.GoogleUserInfo;
import com.moyeo.backend.auth.infrastructure.config.OAuthProviderConfigProperties;
import com.moyeo.backend.auth.infrastructure.dtos.GoogleResponseDto;
import com.moyeo.backend.auth.infrastructure.dtos.TokenResponse;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j(topic = "GoogleAuthService")
@Service("google")
public class GoogleAuthService extends AbstractOAuthProviderService{

    public GoogleAuthService(OAuthProviderConfigProperties properties) {
        super(properties.getProviders().get("google"));
    }

    @Override
    public TokenResponse getAccessToken(String authorizationCode) {
        MultiValueMap<String, String> params = settingParams(authorizationCode);
        params.add("client_secret", config.getClientSecret());

        try {
            ResponseEntity<TokenResponse> response = requestToken(params);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("구글 token API 요청 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.OAUTH_API_CALL_FAILED);
        }
    }

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = settingAuthHeader(accessToken);
        try {
            ResponseEntity<GoogleResponseDto> response = requestUserInfo(
                    headers,
                    GoogleResponseDto.class
            );

            return GoogleUserInfo.builder()
                    .response(response.getBody())
                    .build();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                log.error("잘못된 구글 토큰 사용 : {}", e.getMessage());
                throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
            }
            log.error("구글 사용자 정보 조회 API 요청 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.OAUTH_API_CALL_FAILED);
        }
    }

}
