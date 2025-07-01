package com.moyeo.backend.auth.infrastructure.client;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.infrastructure.dtos.TokenResponse;

public interface OAuthProviderService {
    TokenResponse getAccessToken(String authorizationCode);

    OAuthUserInfo getUserInfo(String accessToken);
}
