package com.moyeo.backend.challenge.participation.infrastructure.repository;

import com.moyeo.backend.challenge.participation.domain.ChallengeParticipationRepository;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaChallengeParticipationRepository extends ChallengeParticipationRepository, JpaRepository<ChallengeParticipation, UUID>, CustomChallengeParticipationRepository {
}
