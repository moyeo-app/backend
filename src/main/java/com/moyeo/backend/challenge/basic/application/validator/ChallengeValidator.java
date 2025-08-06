package com.moyeo.backend.challenge.basic.application.validator;

import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.infrastructure.repository.JpaChallengeInfoRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ChallengeValidator {

    private final JpaChallengeInfoRepository challengeInfoRepository;

    public Challenge getValidChallengeById(String challengeId) {
        return challengeInfoRepository.findByIdAndIsDeletedFalse(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    }

    // 시작일 유효성 검사
    public void validateDate(LocalDate startDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.INVALID_DATE);
        }
    }
}
