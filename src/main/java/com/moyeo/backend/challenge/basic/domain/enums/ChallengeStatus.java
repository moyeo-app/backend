package com.moyeo.backend.challenge.basic.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChallengeStatus {
    RECRUITING("모집 중"),
    CLOSED("모집 마감"),
    INPROGRESS("진행 중"),
    END("챌린지 종료");

    private final String description;
}
