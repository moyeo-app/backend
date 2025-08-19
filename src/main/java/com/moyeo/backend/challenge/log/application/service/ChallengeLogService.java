package com.moyeo.backend.challenge.log.application.service;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogKeywordRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogResponseDto;

public interface ChallengeLogService {

    ChallengeLogResponseDto create(String challengeId, ChallengeLogKeywordRequestDto requestDto);
}
