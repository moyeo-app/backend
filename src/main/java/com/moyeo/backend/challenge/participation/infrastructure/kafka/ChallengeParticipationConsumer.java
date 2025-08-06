package com.moyeo.backend.challenge.participation.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.infrastructure.repository.JpaChallengeInfoRepository;
import com.moyeo.backend.challenge.participation.application.mapper.ChallengeParticipationMapper;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.challenge.participation.infrastructure.repository.JpaChallengeParticipationRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.infrastructure.JpaPaymentRepository;
import com.moyeo.backend.user.domain.User;
import com.moyeo.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChallengeParticipationConsumer {

    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;
    private final JpaChallengeParticipationRepository participationRepository;
    private final JpaChallengeInfoRepository challengeInfoRepository;
    private final JpaPaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ChallengeParticipationMapper participationMapper;

    @KafkaListener(topics = "challenge.participation.complete", groupId = "challenge-consumer")
    @Transactional
    public void consume(String messageJson) {
        try {
            ChallengeParticipationEvent message = objectMapper.readValue(messageJson, ChallengeParticipationEvent.class);
            String challengeId = message.getChallengeId();
            String userId = message.getUserId();
            String paymentId = message.getPaymentId();

            String pendingKey = buildPendingKey(challengeId, userId);
            if (!redisTemplate.hasKey(pendingKey)) {
                log.warn("[Kafka] 참여 실패 - TTL 만료된 요청입니다. userId={}, challengeId={}", userId, challengeId);
                return;
            }

            Challenge challenge = challengeInfoRepository.findByIdAndIsDeletedFalse(challengeId)
                    .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
            User user = userRepository.findByIdAndIsDeletedFalse(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            PaymentHistory payment = paymentRepository.findByIdAndIsDeletedFalse(paymentId)
                    .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

            ChallengeParticipation participation = participationMapper.toParticipant(challenge, user);
            payment.updateChallenge(participation);
            challenge.updateParticipantsCount();

            participationRepository.save(participation);
            redisTemplate.delete(pendingKey);

            log.info("[Kafka] 참여 확정 완료 userId={}, challengeId={}", userId, challengeId);
        } catch (Exception e) {
            log.error("[Kafka] 참여 확정 처리 중 예외 발생", e);
        }
    }

    private String buildPendingKey(String challengeId, String userId) {
        return "challenge:" + challengeId + ":pending:" + userId;
    }
}

