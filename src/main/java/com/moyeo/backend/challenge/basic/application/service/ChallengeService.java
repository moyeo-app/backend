package com.moyeo.backend.challenge.basic.application.service;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeResponseDto;
import com.moyeo.backend.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ChallengeService {

    ChallengeResponseDto create(ChallengeCreateRequestDto requestDto);

    ChallengeReadResponseDto getById(String challengeId);

    PageResponse<ChallengeReadResponseDto> gets(ChallengeReadRequestDto requestDto, Pageable pageable);

    void updateStatus(LocalDate date);
}
