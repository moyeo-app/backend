package com.moyeo.backend.study.infrastructure.batch;

import com.moyeo.backend.common.batch.LoggingJobExecutionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class StudyBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final LoggingJobExecutionListener loggingJobExecutionListener;

    @Bean
    public Step aggregateStudyStep(StudyTasklet tasklet) {
        return new StepBuilder("aggregateStudyStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Job studyCalendarJob(@Qualifier("aggregateStudyStep") Step aggregateStudyStep) {
        return new JobBuilder("studyCalendarJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(loggingJobExecutionListener)
                .start(aggregateStudyStep)
                .build();
    }
}
