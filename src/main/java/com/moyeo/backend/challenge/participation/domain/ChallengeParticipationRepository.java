package com.moyeo.backend.challenge.participation.domain;

import java.util.Optional;

public interface ChallengeParticipationRepository {
    Optional<ChallengeParticipation> findByChallengeIdAndIsDeletedFalse(String challengeId);
}
