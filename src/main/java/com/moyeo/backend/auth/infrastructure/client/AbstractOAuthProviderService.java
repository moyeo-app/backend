package com.moyeo.backend.auth.infrastructure.client;

import com.moyeo.backend.auth.infrastructure.config.OAuthProviderConfig;
import com.moyeo.backend.auth.infrastructure.dtos.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public abstract class AbstractOAuthProviderService implements OAuthProviderService {

    protected final OAuthProviderConfig config;

    protected MultiValueMap<String, String> settingParams(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", config.getClientId());
        params.add("redirect_uri", config.getRedirectUri());
        params.add("code", authorizationCode);

        return params;
    }

    protected ResponseEntity<TokenResponse> requestToken(MultiValueMap<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        return new RestTemplate().postForEntity(
                config.getTokenUri(),
                request,
                TokenResponse.class
        );
    }
    protected HttpHeaders settingAuthHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        return headers;
    }

    protected <T> ResponseEntity<T> requestUserInfo(HttpHeaders headers, Class<T> responseType) {
        return new RestTemplate().exchange(
                config.getUserInfoUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                responseType
        );
    }
}
