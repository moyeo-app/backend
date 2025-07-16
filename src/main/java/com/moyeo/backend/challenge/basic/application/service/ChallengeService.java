package com.moyeo.backend.challenge.basic.application.service;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeResponseDto;

public interface ChallengeService {

    ChallengeResponseDto create(ChallengeCreateRequestDto requestDto);

    ChallengeReadResponseDto getById(String id);
}
