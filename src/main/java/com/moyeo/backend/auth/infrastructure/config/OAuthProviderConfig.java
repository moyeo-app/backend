package com.moyeo.backend.auth.infrastructure.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthProviderConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri;
    private String userInfoUri;
}
