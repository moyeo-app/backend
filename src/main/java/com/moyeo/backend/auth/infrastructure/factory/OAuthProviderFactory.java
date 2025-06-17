package com.moyeo.backend.auth.infrastructure.factory;

import com.moyeo.backend.auth.infrastructure.client.OAuthProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuthProviderFactory {

    private final Map<String, OAuthProviderService> serviceMap;

    public OAuthProviderService getProvider(String provider) {
        return serviceMap.get(provider);
    }
}
