package com.moyeo.backend.auth.infrastructure;

import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaOauthRepository extends OauthRepository, JpaRepository<Oauth, UUID> {
}
