package com.moyeo.backend.challenge.log.infrastructure.repository;

import com.moyeo.backend.challenge.log.domain.ChallengeLog;
import com.moyeo.backend.challenge.log.domain.ChallengeLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaChallengeRepository extends ChallengeLogRepository, JpaRepository<ChallengeLog, UUID> {
}
