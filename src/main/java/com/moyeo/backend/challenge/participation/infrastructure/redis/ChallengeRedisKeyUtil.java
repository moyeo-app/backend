package com.moyeo.backend.challenge.participation.infrastructure.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeRedisKeyUtil {

    private static final String PREFIX = "challenge";

    public static String buildSlotsKey(String challengeId) {
        return String.format("%s:%s:slots", PREFIX, challengeId);
    }

    public static String buildPendingKey(String challengeId, String userId) {
        return String.format("%s:%s:pending:%s", PREFIX, challengeId, userId);
    }
}
