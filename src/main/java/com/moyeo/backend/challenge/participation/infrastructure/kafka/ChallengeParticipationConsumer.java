package com.moyeo.backend.challenge.participation.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeo.backend.challenge.basic.application.validator.ChallengeValidator;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.participation.application.mapper.ChallengeParticipationMapper;
import com.moyeo.backend.challenge.participation.application.validator.ChallengeParticipationValidator;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.challenge.participation.infrastructure.redis.ChallengeRedisKeyUtil;
import com.moyeo.backend.challenge.participation.infrastructure.repository.JpaChallengeParticipationRepository;
import com.moyeo.backend.payment.application.validator.PaymentValidator;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.user.application.validator.UserValidator;
import com.moyeo.backend.user.domain.User;
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
    private final ChallengeParticipationMapper participationMapper;
    private final ChallengeValidator challengeValidator;
    private final UserValidator userValidator;
    private final PaymentValidator paymentValidator;
    private final ChallengeParticipationValidator participationValidator;

    @KafkaListener(topics = "challenge.participation.complete", groupId = "challenge-consumer")
    @Transactional
    public void consume(String messageJson) throws JsonProcessingException {
        ChallengeParticipationEvent message = objectMapper.readValue(messageJson, ChallengeParticipationEvent.class);
        String challengeId = message.getChallengeId();
        String userId = message.getUserId();
        String paymentId = message.getPaymentId();

        String pendingKey = ChallengeRedisKeyUtil.buildPendingKey(challengeId, userId);
        participationValidator.validateHasPending(pendingKey, challengeId, userId);
        participationValidator.validateNotParticipated(challengeId, userId);

        Challenge challenge = challengeValidator.getValidChallengeById(challengeId);
        User user = userValidator.getValidUserById(userId);
        PaymentHistory payment = paymentValidator.getValidPaymentByIdAndUserId(paymentId, userId);

        ChallengeParticipation participation = participationMapper.toParticipant(challenge, user);
        payment.updateParticipation(participation);
        challenge.updateParticipantsCount();

        participationRepository.save(participation);
        redisTemplate.delete(pendingKey);

        log.info("[Kafka] 참여 확정 완료 userId={}, challengeId={}", userId, challengeId);
    }
}

