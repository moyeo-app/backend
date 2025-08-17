package com.moyeo.backend.challenge.participation.infrastructure.repository;

import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomChallengeParticipationRepository {

    Page<ChallengeParticipationReadResponseDto> findMyParticipation(String userId, ChallengeParticipationReadRequestDto requestDto, Pageable pageable);
}
