package com.moyeo.backend.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommonBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job studyCalendarJob;
    private final Job statusTransitionJob;
    private final Clock clock;

    @Scheduled(cron = "0 3 0 * * *", zone = "Asia/Seoul")
    public void runForStudy() throws Exception {
        LocalDate target = LocalDate.now(clock).minusDays(1);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("targetDate", target.toString())
                .addLong("ts", System.currentTimeMillis())
                .toJobParameters();

        run(studyCalendarJob, jobParameters, "총 공부 시간/주간 학습 집계/루틴 리포트/주간 참여 상태 업데이트");
    }

    @Scheduled(cron = "0 1 0 * * *", zone = "Asia/Seoul")
    public void runChallengeStatusTransition() throws Exception {
        LocalDate target = LocalDate.now(clock);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("targetDate", target.toString())
                .addLong("ts", System.currentTimeMillis())
                .toJobParameters();

        run(statusTransitionJob, jobParameters, "챌린지 상태 전환");
    }

    private void run(Job job, JobParameters params, String logTitle) {
        try {
            jobLauncher.run(job, params);
            log.info("[Batch] {} 스케줄러 작동 params={}", logTitle, params);
        } catch (Exception e) {
            log.error("[Batch] {} 스케줄러 실패 params={}", logTitle, params, e);
        }
    }

}
