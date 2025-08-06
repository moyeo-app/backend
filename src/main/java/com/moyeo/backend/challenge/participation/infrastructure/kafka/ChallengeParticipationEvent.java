package com.moyeo.backend.challenge.participation.infrastructure.kafka;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChallengeParticipationEvent {
    private String challengeId;
    private String userId;
    private String paymentId;
}
