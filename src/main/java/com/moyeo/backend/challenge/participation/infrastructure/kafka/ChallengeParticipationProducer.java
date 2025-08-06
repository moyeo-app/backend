package com.moyeo.backend.challenge.participation.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "ChallengeParticipationProducer")
public class ChallengeParticipationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "challenge.participation.complete";

    public void sendParticipationCompleteEvent(ChallengeParticipationEvent event) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(TOPIC, event.getUserId(), message);
        log.info("[Kafka] 참여 확정 메시지 전송: {}", message);
    }
}
