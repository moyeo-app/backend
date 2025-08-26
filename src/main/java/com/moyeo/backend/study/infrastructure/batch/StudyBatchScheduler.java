package com.moyeo.backend.study.infrastructure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudyBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job studyCalendarJob;

    @Scheduled(cron = "0 1 0 * * *", zone = "Asia/Seoul")
    public void runForStudy() {
        LocalDate target = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("targetDate", target.toString())
                .addLong("ts", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(studyCalendarJob, jobParameters);
            log.info("[Batch] 총 공부 시간 업데이트 스케줄러 작동 date = {}", target);
        } catch (Exception e) {
            log.error("[Batch] 총 공부 시간 업데이트 스케줄러 작동 실패 date = {}", target, e);
        }
    }
}
