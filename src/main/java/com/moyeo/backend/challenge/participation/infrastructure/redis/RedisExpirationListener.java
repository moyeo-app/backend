package com.moyeo.backend.challenge.participation.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisExpirationListener {

    private static final String CHALLENGE_PREFIX = "challenge";
    private static final String PENDING_PART     = "pending";
    private static final String SLOTS_SUFFIX     = "slots";

    private final StringRedisTemplate redisTemplate;

    @Value("${spring.data.redis.database:0}")
    private int redisDatabase;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        // 환경설정에 따른 DB index 사용
        container.addMessageListener(
                expiredEventListener(),
                new PatternTopic(String.format("__keyevent@%d__:expired", redisDatabase))
        );
        return container;
    }

    @Bean
    public MessageListenerAdapter expiredEventListener() {
        return new MessageListenerAdapter((MessageListener) (message, pattern) -> {
            try {
                String expiredKey = message.toString();
                log.info("[Redis] TTL 만료 감지: {}", expiredKey);

                String[] parts = expiredKey.split(":");
                if (parts.length == 4
                        && CHALLENGE_PREFIX.equals(parts[0])
                        && PENDING_PART.equals(parts[2])) {

                    String challengeId = parts[1];
                    String slotsKey = CHALLENGE_PREFIX + ":" + challengeId + ":" + SLOTS_SUFFIX;

                    if (!redisTemplate.hasKey(slotsKey)) {
                        log.warn("[Redis] 슬롯 키 없으므로 복구 스킵, challengeId = {})", challengeId);
                        return;
                    }

                    redisTemplate.opsForValue().increment(slotsKey);
                    log.info("[Redis] TTL 만료 복구 완료, challengeId = {}", challengeId);
                }
            } catch (Exception e) {
                log.error("[Redis] Expired key 처리 중 예외 발생", e);
            }
        });
    }
}