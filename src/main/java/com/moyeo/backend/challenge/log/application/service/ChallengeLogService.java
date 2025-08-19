package com.moyeo.backend.challenge.log.application.service;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogContentRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogKeywordRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogResponseDto;

public interface ChallengeLogService {

    ChallengeLogResponseDto create(String challengeId, ChallengeLogKeywordRequestDto requestDto);

    ChallengeLogResponseDto update(String challengeId, String logId, ChallengeLogContentRequestDto requestDto);
}
