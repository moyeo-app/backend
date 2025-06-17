package com.moyeo.backend.auth.infrastructure;

import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j(topic = "KakaoService")
@Service
public class KakaoService {
    private static final String API_BASE_URL = "https://kapi.kakao.com/v2/user/me";

    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = settingHeader(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserInfoDto> response = restTemplate.exchange(
                    API_BASE_URL,
                    HttpMethod.GET,
                    request,
                    KakaoUserInfoDto.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                log.error("만료된 카카오 토큰 사용 : {}", e.getMessage());
                throw new CustomException(ErrorCode.EXPIRED_KAKAO_TOKEN);
            }
            log.error("카카오 API 요청 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.KAKAO_API_CALL_FAILED);
        }
    }

    private HttpHeaders settingHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        return headers;
    }
}
