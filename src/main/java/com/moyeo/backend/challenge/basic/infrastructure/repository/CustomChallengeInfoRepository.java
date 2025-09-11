package com.moyeo.backend.challenge.basic.infrastructure.repository;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomChallengeInfoRepository {

    Page<ChallengeReadResponseDto> searchChallenges(ChallengeReadRequestDto requestDto, Pageable pageable);

    void updateStatus(LocalDate date);
}
