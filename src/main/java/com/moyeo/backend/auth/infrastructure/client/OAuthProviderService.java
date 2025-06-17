package com.moyeo.backend.auth.infrastructure.client;

import com.moyeo.backend.auth.domain.OAuthUserInfo;

public interface OAuthProviderService {
    OAuthUserInfo getUserInfo(String accessToken);
}
