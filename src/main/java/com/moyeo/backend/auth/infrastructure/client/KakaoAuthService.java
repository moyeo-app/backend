package com.moyeo.backend.auth.infrastructure.client;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.infrastructure.adapter.KakaoUserInfo;
import com.moyeo.backend.auth.infrastructure.dtos.KakaoResponseDto;
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

@Slf4j(topic = "KakaoService")
@Service("kakao")
public class KakaoAuthService extends AbstractOAuthProviderService {
    private static final String API_BASE_URL_TOKEN = "https://kauth.kakao.com/oauth/token";
    private static final String API_BASE_URL_INFO = "https://kapi.kakao.com/v2/user/me";

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    public TokenResponse getAccessToken(String authorizationCode) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
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
            log.error("카카오 token API 요청 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.OAUTH_API_CALL_FAILED);
        }
    }

    public OAuthUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = settingHeader(accessToken);

        try {
            ResponseEntity<KakaoResponseDto> response = requestUserInfo(
                    API_BASE_URL_INFO,
                    headers,
                    KakaoResponseDto.class
            );

            return KakaoUserInfo.builder()
                    .response(response.getBody())
                    .build();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                log.error("만료된 카카오 토큰 사용 : {}", e.getMessage());
                throw new CustomException(ErrorCode.EXPIRED_OAUTH_TOKEN);
            }
            log.error("카카오 사용자 정보 조회 API 요청 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.OAUTH_API_CALL_FAILED);
        }
    }
}
