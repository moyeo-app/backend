package com.moyeo.backend.challenge.basic.domain.repository;

import com.moyeo.backend.challenge.basic.domain.Challenge;

public interface ChallengeInfoRepository {
    Challenge save(Challenge challenge);
}
