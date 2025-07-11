package com.moyeo.backend.challenge.basic.infrastructure.repository;

import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.repository.ChallengeInfoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaChallengeInfoRepository extends ChallengeInfoRepository, JpaRepository<Challenge, UUID> {
}
