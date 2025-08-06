package com.moyeo.backend.challenge.participation.application.validator;

import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.challenge.participation.infrastructure.repository.JpaChallengeParticipationRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChallengeParticipationValidator {

    private final JpaChallengeParticipationRepository participationRepository;

    public void validateNotParticipated(String challengeId, String userId) {
        Optional<ChallengeParticipation> participation = participationRepository.findByChallengeIdAndUserIdAndIsDeletedFalse(challengeId, userId);
        if (participation.isPresent()) {
            throw new CustomException(ErrorCode.PARTICIPATION_ALREADY_EXISTS);
        }
    }
}
