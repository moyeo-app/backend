package com.moyeo.backend.challenge.participation.application.service;

import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadResponseDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;
import com.moyeo.backend.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ChallengeParticipationService {

    Boolean check(String challengeId);

    void participate(String challengeId, ChallengeParticipationRequestDto requestDto);

    PageResponse<ChallengeParticipationReadResponseDto> gets(ChallengeParticipationReadRequestDto requestDto, Pageable pageable);

    void updateStatus(LocalDate date);
}
