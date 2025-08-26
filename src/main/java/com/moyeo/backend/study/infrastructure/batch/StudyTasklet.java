package com.moyeo.backend.study.infrastructure.batch;

import com.moyeo.backend.study.application.service.StudyService;
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

@Slf4j(topic = "StudyTasklet")
@Component
@StepScope
@RequiredArgsConstructor
public class StudyTasklet implements Tasklet {

    private final StudyService studyService;

    @Value("#{jobParameters['targetDate']}")
    private String targetDate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDate date = LocalDate.parse(targetDate);
        studyService.aggregate(date);
        log.info("[Batch] 총 공부 시간 업데이트 배치 완료 date = {}", date);
        return RepeatStatus.FINISHED;
    }
}
