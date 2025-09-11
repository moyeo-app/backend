package com.moyeo.backend.challenge.batch;

import com.moyeo.backend.challenge.basic.application.service.ChallengeService;
import com.moyeo.backend.challenge.participation.application.service.ChallengeParticipationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j(topic = "ChallengeStatusTransitionTasklet")
@Component
@StepScope
@RequiredArgsConstructor
public class ChallengeStatusTransitionTasklet implements Tasklet {

    private final ChallengeService challengeService;

    @Value("#{jobParameters['targetDate']}")
    private String targetDate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDate date = LocalDate.parse(targetDate);
        challengeService.updateStatus(date);
        return RepeatStatus.FINISHED;
    }
}
