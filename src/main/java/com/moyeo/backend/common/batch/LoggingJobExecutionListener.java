package com.moyeo.backend.common.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public class LoggingJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("[Batch:Start] job = {}, params = {}", jobExecution.getJobInstance().getJobName(), jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LocalDateTime startTime = jobExecution.getStartTime();
        LocalDateTime endTime = jobExecution.getEndTime();
        Duration duration = (startTime != null && endTime != null) ? Duration.between(startTime, endTime) : null;

        log.info("[Batch:End] job={}, status = {}, exitCode = {}, durationMs = {}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus(),
                jobExecution.getExitStatus(),
                (duration != null ? duration.toMillis() : null));
    }
}