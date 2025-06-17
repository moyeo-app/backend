package com.moyeo.backend.auth.infrastructure.client;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.infrastructure.adapter.KakaoUserInfo;
import com.moyeo.backend.auth.infrastructure.dtos.KakaoResponseDto;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j(topic = "KakaoService")
@Service("kakao")
public class KakaoAuthService extends AbstractOAuthProviderService {
    private static final String API_BASE_URL = "https://kapi.kakao.com/v2/user/me";

    public OAuthUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = settingHeader(accessToken);

        try {
            ResponseEntity<KakaoResponseDto> response = requestUserInfo(API_BASE_URL, headers, KakaoResponseDto.class);

            return KakaoUserInfo.builder()
                    .response(response.getBody())
                    .build();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                log.error("만료된 카카오 토큰 사용 : {}", e.getMessage());
                throw new CustomException(ErrorCode.EXPIRED_OAUTH_TOKEN);
            }
            log.error("카카오 API 요청 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.OAUTH_API_CALL_FAILED);
        }
    }
}
