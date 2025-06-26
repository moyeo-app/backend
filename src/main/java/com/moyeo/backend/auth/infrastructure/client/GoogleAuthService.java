package com.moyeo.backend.auth.infrastructure.client;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.infrastructure.adapter.GoogleUserInfo;
import com.moyeo.backend.auth.infrastructure.dtos.GoogleResponseDto;
import com.moyeo.backend.auth.infrastructure.dtos.TokenResponse;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j(topic = "GoogleAuthService")
@Service("google")
public class GoogleAuthService extends AbstractOAuthProviderService{
    private static final String API_BASE_URL_TOKEN = "https://oauth2.googleapis.com/token";
    private static final String API_BASE_URL_INFO = "https://www.googleapis.com/oauth2/v3/userinfo";

    @Value("${google.client_id}")
    private String clientId;

    @Value("${google.client_secret}")
    private String clientSecret;

    @Value("${google.redirect_uri}")
    private String redirectUri;

    @Override
    public TokenResponse getAccessToken(String authorizationCode) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                    API_BASE_URL_TOKEN,
                    request,
                    TokenResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("구글 token API 요청 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.OAUTH_API_CALL_FAILED);
        }
    }

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = settingHeader(accessToken);

        try {
            ResponseEntity<GoogleResponseDto> response = requestUserInfo(API_BASE_URL_INFO, headers, GoogleResponseDto.class);

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

}
