package com.moyeo.backend.challenge.batch;

import com.moyeo.backend.common.batch.LoggingJobExecutionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ChallengeBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final LoggingJobExecutionListener loggingJobExecutionListener;

    private final ChallengeStatusTransitionTasklet challengeStatusTransitionTasklet;
    private final ParticipationStatusTransitionTasklet participationStatusTransitionTasklet;

    /*
    * 챌린지 상태 전환 (RECRUITING -> INPROGRESS, INPROGRESS -> END)
     */
    @Bean
    public Step challengeStatusTransitionStep() {
        return new StepBuilder("challengeStatusTransitionStep", jobRepository)
                .tasklet(challengeStatusTransitionTasklet, transactionManager)
                .build();
    }

    /*
     * 챌린지 참여 상태 전환 (NULL -> INPROGRESS, INPROGRESS -> END)
     */
    @Bean
    public Step participationStatusTransitionStep() {
        return new StepBuilder("participationStatusTransitionStep", jobRepository)
                .tasklet(participationStatusTransitionTasklet, transactionManager)
                .build();
    }

    @Bean
    public Job statusTransitionJob() {
        return new JobBuilder("StatusTransitionJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(loggingJobExecutionListener)
                .start(challengeStatusTransitionStep())
                .next(participationStatusTransitionStep())
                .build();
    }
}
