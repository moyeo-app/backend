package com.moyeo.backend.challenge.participation.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.infrastructure.repository.JpaChallengeInfoRepository;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.challenge.participation.infrastructure.kafka.ChallengeParticipationEvent;
import com.moyeo.backend.challenge.participation.infrastructure.kafka.ChallengeParticipationProducer;
import com.moyeo.backend.challenge.participation.infrastructure.repository.JpaChallengeParticipationRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.infrastructure.JpaPaymentRepository;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Slf4j(topic = "ChallengeParticipationService")
@Service
@RequiredArgsConstructor
public class ChallengeParticipationServiceImpl implements ChallengeParticipationService {

    private final UserContextService userContextService;
    private final JpaChallengeParticipationRepository participationRepository;
    private final JpaChallengeInfoRepository challengeInfoRepository;
    private final JpaPaymentRepository paymentRepository;
    private final StringRedisTemplate redisTemplate;
    private final ChallengeParticipationProducer participationProducer;

    private static final Duration PENDING_TTL = Duration.ofMinutes(5);

    @Override
    @Transactional
    public Boolean check(String challengeId) {
        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();

        Challenge challenge = validateChallengeNotParticipated(challengeId, userId);
        reserveSlotOrFail(challengeId, challenge, userId);
        return true;
    }

    @Override
    @Transactional
    public void participate(String challengeId, ChallengeParticipationRequestDto requestDto) {
        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();

        validateChallengeNotParticipated(challengeId, userId);
        validatePaymentOwnership(requestDto.getPaymentId(), userId);

        ChallengeParticipationEvent event = ChallengeParticipationEvent.builder()
                .challengeId(challengeId)
                .userId(userId)
                .paymentId(requestDto.getPaymentId())
                .build();

        try {
            participationProducer.sendParticipationCompleteEvent(event);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.KAFKA_SEND_FAILED);
        }
    }

    private Challenge validateChallengeNotParticipated(String challengeId, String userId) {
        Challenge challenge = challengeInfoRepository.findByIdAndIsDeletedFalse(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        Optional<ChallengeParticipation> participation = participationRepository.findByChallengeIdAndUserIdAndIsDeletedFalse(challengeId, userId);
        if (participation.isPresent()) {
            throw new CustomException(ErrorCode.PARTICIPATION_ALREADY_EXISTS);
        }

        // TODO: 동일 시간대에 참여하고 있는 챌린지 있는지도 확인해야함

        return challenge;
    }

    private void validatePaymentOwnership(String paymentId, String userId) {
        PaymentHistory payment = paymentRepository.findByIdAndIsDeletedFalse(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
        if (!payment.getOrderId().contains(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    private void reserveSlotOrFail(String challengeId, Challenge challenge, String userId) {
        String slotsKey = buildSlotsKey(challengeId);
        String pendingKey = buildPendingKey(challengeId, userId);

        if (!redisTemplate.hasKey(slotsKey)) {
            int remaining = challenge.getMaxParticipants() - challenge.getParticipantsCount();
            redisTemplate.opsForValue().set(slotsKey, String.valueOf(remaining));
        }

        Long remain = redisTemplate.opsForValue().decrement(slotsKey);
        if (remain == null || remain < 0) {
            throw new CustomException(ErrorCode.CHALLENGE_PARTICIPATION_CLOSED);
        }

        redisTemplate.opsForValue().set(pendingKey, "true", PENDING_TTL);
    }

    private String buildSlotsKey(String challengeId) {
        return "challenge:" + challengeId + ":slots";
    }

    private String buildPendingKey(String challengeId, String userId) {
        return "challenge:" + challengeId + ":pending:" + userId;
    }
}
