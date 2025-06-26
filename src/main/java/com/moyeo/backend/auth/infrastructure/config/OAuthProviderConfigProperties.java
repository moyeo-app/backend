package com.moyeo.backend.auth.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "oauth")
@Data
public class OAuthProviderConfigProperties {
    private Map<String, OAuthProviderConfig> providers;
}
