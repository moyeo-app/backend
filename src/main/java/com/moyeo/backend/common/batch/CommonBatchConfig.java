package com.moyeo.backend.common.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
@RequiredArgsConstructor
public class CommonBatchConfig {

    @Bean
    public ZoneId zoneId() {
        return ZoneId.of("Asia/Seoul");
    }

    @Bean
    public Clock clock(ZoneId zoneId) {
        return Clock.system(zoneId);
    }

    @Bean
    public LoggingJobExecutionListener loggingJobExecutionListener() {
        return new LoggingJobExecutionListener();
    }
}