package com.moyeo.backend.challenge.participation.domain;

import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ChallengeParticipationRepository {
    Optional<ChallengeParticipation> findByChallengeIdAndUserIdAndIsDeletedFalse(String challengeId, String userId);

    ChallengeParticipation save(ChallengeParticipation participation);

    Page<ChallengeParticipationReadResponseDto> findMyParticipation(String userId, ChallengeParticipationReadRequestDto requestDto, Pageable pageable);

    boolean existsByChallengeIdAndUserIdAndIsDeletedFalse(String challengeId, String userId);

    void updateStatus(LocalDate date);

    void updateWeeklyStatus(LocalDate date);
}
