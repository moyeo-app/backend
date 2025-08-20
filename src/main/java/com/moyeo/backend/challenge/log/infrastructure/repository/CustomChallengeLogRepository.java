package com.moyeo.backend.challenge.log.infrastructure.repository;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogReadRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogReadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomChallengeLogRepository {

    Page<ChallengeLogReadResponseDto> getLogs(String challengeId, ChallengeLogReadRequestDto requestDto, Pageable pageable);
}
