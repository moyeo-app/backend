package com.moyeo.backend.challenge.log.application.service;

import com.moyeo.backend.challenge.log.application.dto.*;
import com.moyeo.backend.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ChallengeLogService {

    ChallengeLogResponseDto create(String challengeId, ChallengeLogKeywordRequestDto requestDto);

    ChallengeLogResponseDto update(String challengeId, String logId, ChallengeLogContentRequestDto requestDto);

    PageResponse<ChallengeLogReadResponseDto> gets(String challengeId, ChallengeLogReadRequestDto requestDto, Pageable pageable);

    ChallengeLogReadResponseDto getByUser(String challengeId);
}
