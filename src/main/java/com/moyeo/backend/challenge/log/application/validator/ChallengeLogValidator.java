package com.moyeo.backend.challenge.log.application.validator;

import com.moyeo.backend.challenge.log.domain.ChallengeLog;
import com.moyeo.backend.challenge.log.domain.ChallengeLogContent;
import com.moyeo.backend.challenge.log.domain.ChallengeLogRepository;
import com.moyeo.backend.challenge.log.domain.ContentLog;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Slf4j(topic = "ChallengeLogValidator")
@Component
@RequiredArgsConstructor
public class ChallengeLogValidator {

    private final ChallengeLogRepository logRepository;

    public ChallengeLog getValidLogById(String logId) {
        return logRepository.findByIdAndIsDeletedFalse(logId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_LOG_NOT_FOUND));
    }

    public void validLogOwnership(String participationId, String logParticipationId) {
        if (!participationId.equals(logParticipationId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    public void validText(String logId, String text) {
        ChallengeLog challengeLog = getValidLogById(logId);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (!today.equals(challengeLog.getDate())) {
            throw new CustomException(ErrorCode.CHALLENGE_LOG_DATE_MISMATCH);
        }

        // 입력된 내용에 키워드가 포함되어 있는지 확인
        ChallengeLogContent content = challengeLog.getContent();

        if (content instanceof ContentLog contentLog) {
            List<String> keywords = contentLog.getKeywords();

            List<String> missing = keywords.stream()
                    .filter(k -> !containsIgnoreCase(text, k))
                    .collect(Collectors.toList());

            if (!missing.isEmpty()) {
                throw new CustomException(ErrorCode.CONTENT_KEYWORDS_NOT_FOUND);
            }
        } else {
            throw new CustomException(ErrorCode.CHALLENGE_LOG_CONTENT_TYPE_MISMATCH);
        }
    }
}
