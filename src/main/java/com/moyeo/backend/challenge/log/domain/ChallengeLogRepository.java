package com.moyeo.backend.challenge.log.domain;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogReadRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogReadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChallengeLogRepository {

    ChallengeLog save(ChallengeLog challengeLog);

    Optional<ChallengeLog> findByIdAndIsDeletedFalse(String logId);

    Page<ChallengeLogReadResponseDto> getLogs(String challengeId, ChallengeLogReadRequestDto requestDto, Pageable pageable);
}
