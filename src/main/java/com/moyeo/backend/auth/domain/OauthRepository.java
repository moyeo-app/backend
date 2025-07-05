package com.moyeo.backend.auth.domain;

import java.util.Optional;

public interface OauthRepository {
    Oauth save(Oauth oauth);

    Optional<Oauth> findByOauthIdAndProviderAndIsDeletedFalse(String oauthId, Provider provider);

    Optional<Oauth> findByOauthIdAndIsDeletedFalse(String oauthId);
}
