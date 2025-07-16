package com.moyeo.backend.challenge.basic.domain.repository;

import com.moyeo.backend.challenge.basic.domain.Challenge;

import java.util.Optional;

public interface ChallengeInfoRepository {
    Challenge save(Challenge challenge);

    Optional<Challenge> findByIdAndIsDeletedFalse(String id);
}
