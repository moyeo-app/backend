package com.moyeo.backend.auth.domain;

import java.util.Optional;

public interface OauthRepository {
    Oauth save(Oauth oauth);

    Optional<Oauth> findByOauthIdAndProvider(String oauthId, Provider provider);
}
