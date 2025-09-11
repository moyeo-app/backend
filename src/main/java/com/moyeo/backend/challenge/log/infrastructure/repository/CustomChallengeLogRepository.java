package com.moyeo.backend.challenge.log.infrastructure.repository;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogDailyAggregateDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogReadRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogReadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomChallengeLogRepository {

    Page<ChallengeLogReadResponseDto> getLogs(String challengeId, ChallengeLogReadRequestDto requestDto, Pageable pageable);

    List<ChallengeLogDailyAggregateDto> aggregateDailyByUser(LocalDate date);
}
