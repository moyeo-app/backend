package com.moyeo.backend.auth.domain;

import java.util.Optional;

public interface OauthRepository {
    Optional<Oauth> findByOauthIdAndProviderAndIsDeletedFalse(String oauthId, Provider provider);

    Optional<Oauth> findByOauthIdAndIsDeletedFalse(String oauthId);
}
