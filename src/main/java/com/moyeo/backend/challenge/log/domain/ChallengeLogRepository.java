package com.moyeo.backend.challenge.log.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeLogRepository {

    ChallengeLog save(ChallengeLog challengeLog);
}
