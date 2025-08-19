package com.moyeo.backend.challenge.log.domain;

import java.util.Optional;

public interface ChallengeLogRepository {

    ChallengeLog save(ChallengeLog challengeLog);

    Optional<ChallengeLog> findByIdAndIsDeletedFalse(String logId);
}
