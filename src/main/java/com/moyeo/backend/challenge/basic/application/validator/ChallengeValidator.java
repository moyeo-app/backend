package com.moyeo.backend.challenge.basic.application.validator;

import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.ChallengeOption;
import com.moyeo.backend.challenge.basic.domain.StartEndOption;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeType;
import com.moyeo.backend.challenge.basic.domain.repository.ChallengeInfoRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Slf4j(topic = "ChallengeValidator")
@Component
@RequiredArgsConstructor
public class ChallengeValidator {

    private final ChallengeInfoRepository challengeInfoRepository;

    public Challenge getValidChallengeById(String challengeId) {
        return challengeInfoRepository.findByIdAndIsDeletedFalse(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    }

    // 시작일 유효성 검사
    public void validateChallengeStartDate(LocalDate startDate) {
        if (startDate.isBefore(LocalDate.now().plusDays(1))) {
            throw new CustomException(ErrorCode.INVALID_DATE);
        }
    }

    // 내용 타입 + 날짜 유효성 검사
    public Challenge getValidDateAndContentChallengeById(String challengeId) {
        Challenge challenge = challengeInfoRepository.findByIdAndIsDeletedFalse(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate startDate = challenge.getStartDate();

        log.info("오늘 = {}, 챌린지 시작 날짜 = {}", now, startDate);

        if (now.isBefore(startDate)) {
            throw new CustomException(ErrorCode.CHALLENGE_NOT_IN_TIME);
        }

        if (challenge.getType() != ChallengeType.CONTENT) {
            throw new CustomException(ErrorCode.CHALLENGE_TYPE_MISMATCH);
        }

        return challenge;
    }

    // 키워드 입력 시간 유효성 검사
    public Challenge getValidContentChallengeByIdAndInTime(String challengeId) {
        Challenge challenge = getValidDateAndContentChallengeById(challengeId);

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