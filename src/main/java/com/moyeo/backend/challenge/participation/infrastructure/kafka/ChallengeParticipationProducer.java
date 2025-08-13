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

    public void sendParticipationCompleteEvent(ChallengeParticipationEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            String userId = event.getUserId();
            String challengeId = event.getChallengeId();

            kafkaTemplate.send(TOPIC, userId, message)
                            .whenComplete((result, ex) -> {
                                if (ex != null) {
                                    log.error("[Kafka] 참여 확정 메시지 전송 실패, userId = {}, challengeId = {}, error= {}",
                                            userId, challengeId, ex.getMessage());
                                } else {
                                    log.info("[Kafka] 참여 확정 메시지 전송 성공, userId = {}, challengeId = {}, partition = {}, offset = {}",
                                            userId, challengeId, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                                }
                            });
        } catch (JsonProcessingException e) {
            log.error("[Kafka] 직렬화 오류 실패, userId = {}, challengeId = {}, error= {}",
                    event.getUserId(), event.getChallengeId(), e.getMessage());
        }
    }
}
