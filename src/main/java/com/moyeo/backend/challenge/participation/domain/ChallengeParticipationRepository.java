package com.moyeo.backend.challenge.participation.domain;

import java.util.Optional;

public interface ChallengeParticipationRepository {
    Optional<ChallengeParticipation> findByChallengeIdAndUserIdAndIsDeletedFalse(String challengeId, String userId);

    ChallengeParticipation save(ChallengeParticipation participation);
}
