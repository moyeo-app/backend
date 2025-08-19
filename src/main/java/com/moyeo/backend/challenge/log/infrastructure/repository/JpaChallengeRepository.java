package com.moyeo.backend.challenge.log.infrastructure.repository;

import com.moyeo.backend.challenge.log.domain.ChallengeLog;
import com.moyeo.backend.challenge.log.domain.ChallengeLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaChallengeRepository extends ChallengeLogRepository, JpaRepository<ChallengeLog, UUID> {
}
