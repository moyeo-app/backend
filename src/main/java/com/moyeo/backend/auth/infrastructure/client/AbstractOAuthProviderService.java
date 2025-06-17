package com.moyeo.backend.auth.infrastructure.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractOAuthProviderService implements OAuthProviderService {

    protected HttpHeaders settingHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        return headers;
    }

    protected <T> ResponseEntity<T> requestUserInfo(String url, HttpHeaders headers, Class<T> responseType) {
        return new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                responseType
        );
    }
}
