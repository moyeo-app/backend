package com.moyeo.backend.challenge.basic.application.validator;

import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.ChallengeOption;
import com.moyeo.backend.challenge.basic.domain.StartEndOption;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeType;
import com.moyeo.backend.challenge.basic.infrastructure.repository.JpaChallengeInfoRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class ChallengeValidator {

    private final JpaChallengeInfoRepository challengeInfoRepository;

    public Challenge getValidChallengeById(String challengeId) {
        return challengeInfoRepository.findByIdAndIsDeletedFalse(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    }

    // 시작일 유효성 검사
    public void validateChallengeStartDate(LocalDate startDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.INVALID_DATE);
        }
    }

    // 내용 타입 + 키워드 입력 시간 유효성 검사
    public Challenge getValidContentChallengeById(String challengeId) {
        Challenge challenge = challengeInfoRepository.findByIdAndIsDeletedFalse(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        if (challenge.getType() != ChallengeType.CONTENT) {
            throw new CustomException(ErrorCode.CHALLENGE_TYPE_MISMATCH);
        }

        ChallengeOption option = challenge.getOption();
        if (option instanceof StartEndOption startEndOption) {
            LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
            LocalTime start = startEndOption.getStart();
            LocalTime end = startEndOption.getEnd();

            if (now.isBefore(start) || now.isAfter(end)) {
                throw new CustomException(ErrorCode.CHALLENGE_NOT_IN_TIME);
            }
        } else {
            throw new CustomException(ErrorCode.CHALLENGE_OPTION_TYPE_MISMATCH);
        }
        return challenge;
    }
}
