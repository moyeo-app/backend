package com.moyeo.backend.challenge.participation.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisExpirationListener {

    private final StringRedisTemplate redisTemplate;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        container.addMessageListener(expiredEventListener(), new PatternTopic("__keyevent@0__:expired"));
        return container;
    }

    @Bean
    public MessageListenerAdapter expiredEventListener() {
        return new MessageListenerAdapter(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String expiredKey = message.toString();
                log.info("[Redis] TTL 만료 감지: {}", expiredKey);

                if (expiredKey.startsWith("challenge:")) {
                    String[] parts = expiredKey.split(":");
                    if (parts.length == 4 && "pending".equals(parts[2])) {
                        String challengeId = parts[1];
                        String slotKey = "challenge:" + challengeId + ":slots";
                        redisTemplate.opsForValue().increment(slotKey);
                        log.info("[Redis] TTL 만료된 참여자 복구 - challengeId={} → slots +1", challengeId);
                    }
                }
            }
        });
    }
}
