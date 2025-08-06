package com.moyeo.backend.challenge.participation.application.service;

import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;

public interface ChallengeParticipationService {

    Boolean check(String challengeId);

    void participate(String challengeId, ChallengeParticipationRequestDto requestDto);
}
