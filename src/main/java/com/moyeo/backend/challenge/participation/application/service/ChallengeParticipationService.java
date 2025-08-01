package com.moyeo.backend.challenge.participation.application.service;

import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationResponseDto;

public interface ChallengeParticipationService {

    Boolean check(String challengeId);

    ChallengeParticipationResponseDto participate(String challengeId, ChallengeParticipationRequestDto requestDto);
}
