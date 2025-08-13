package com.moyeo.backend.challenge.participation.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.application.validator.ChallengeValidator;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;
import com.moyeo.backend.challenge.participation.application.validator.ChallengeParticipationValidator;
import com.moyeo.backend.challenge.participation.infrastructure.kafka.ChallengeParticipationEvent;
import com.moyeo.backend.challenge.participation.infrastructure.kafka.ChallengeParticipationProducer;
import com.moyeo.backend.challenge.participation.infrastructure.redis.ChallengeRedisKeyUtil;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.payment.application.validator.PaymentValidator;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j(topic = "ChallengeParticipationService")
@Service
@RequiredArgsConstructor
public class ChallengeParticipationServiceImpl implements ChallengeParticipationService {

    private final UserContextService userContextService;
    private final StringRedisTemplate redisTemplate;
    private final ChallengeParticipationProducer participationProducer;
    private final PaymentValidator paymentValidator;
    private final ChallengeValidator challengeValidator;
    private final ChallengeParticipationValidator participationValidator;

    private static final Duration PENDING_TTL = Duration.ofMinutes(5);

    @Override
    @Transactional
    public Boolean check(String challengeId) {
        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();

        Challenge challenge = challengeValidator.getValidChallengeById(challengeId);
        participationValidator.validateNotParticipated(challengeId, userId);

        // TODO: 동일 시간대에 참여하고 있는 챌린지 있는지도 확인해야함

        reserveSlotOrFail(challengeId, challenge, userId);
        return true;
    }

    @Override
    @Transactional
    public void participate(String challengeId, ChallengeParticipationRequestDto requestDto) {
        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();
        String pendingKey = ChallengeRedisKeyUtil.buildPendingKey(challengeId, userId);
        if (!redisTemplate.hasKey(pendingKey)) {
            throw new CustomException(ErrorCode.NO_PENDING_RESERVATION);
        }

        challengeValidator.getValidChallengeById(challengeId);
        participationValidator.validateNotParticipated(challengeId, userId);
        paymentValidator.getValidPaymentByIdAndUserId(requestDto.getPaymentId(), userId);

        ChallengeParticipationEvent event = ChallengeParticipationEvent.builder()
                .challengeId(challengeId)
                .userId(userId)
                .paymentId(requestDto.getPaymentId())
                .build();

        participationProducer.sendParticipationCompleteEvent(event);
    }

    private void reserveSlotOrFail(String challengeId, Challenge challenge, String userId) {
        String slotsKey = ChallengeRedisKeyUtil.buildSlotsKey(challengeId);
        String pendingKey = ChallengeRedisKeyUtil.buildPendingKey(challengeId, userId);

        int remaining = challenge.getMaxParticipants() - challenge.getParticipantsCount();
        redisTemplate.opsForValue().setIfAbsent(slotsKey, String.valueOf(remaining),PENDING_TTL);

        Long remain = redisTemplate.opsForValue().decrement(slotsKey);
        if (remain == null || remain < 0) {
            if (remain != null && remain < 0) {
                redisTemplate.opsForValue().increment(slotsKey);
            }
            throw new CustomException(ErrorCode.CHALLENGE_PARTICIPATION_CLOSED);
        }

        redisTemplate.opsForValue().set(pendingKey, "true", PENDING_TTL);
    }
}
