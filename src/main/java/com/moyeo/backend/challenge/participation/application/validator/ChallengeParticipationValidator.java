package com.moyeo.backend.challenge.participation.application.validator;

import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipationRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j(topic = "ChallengeParticipationValidator")
@Component
@RequiredArgsConstructor
public class ChallengeParticipationValidator {

    private final ChallengeParticipationRepository participationRepository;
    private final StringRedisTemplate redisTemplate;

    public void validateNotParticipated(String challengeId, String userId) {
        Optional<ChallengeParticipation> participation = participationRepository.findByChallengeIdAndUserIdAndIsDeletedFalse(challengeId, userId);
        if (participation.isPresent()) {
            log.info("[Kafka] 이미 참여중, challengeId = {}, userId = {}", challengeId, userId);
            throw new CustomException(ErrorCode.PARTICIPATION_ALREADY_EXISTS);
        }
    }

    public void validateHasPending(String pendingKey, String challengeId, String userId) {
        if (!redisTemplate.hasKey(pendingKey)) {
            log.warn("[Kafka] 참여 실패 - Pending 없음/TTL 만료 요청, challengeId = {}, userId = {}", challengeId, userId);
            throw new CustomException(ErrorCode.NO_PENDING_RESERVATION);
        }
    }

    public ChallengeParticipation getValidParticipationByUserId(String challengeId, String userId) {
        return participationRepository.findByChallengeIdAndUserIdAndIsDeletedFalse(challengeId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTICIPATION_NOT_FOUND));
    }
}
