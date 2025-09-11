package com.moyeo.backend.challenge.participation.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ParticipationStatus {
    INPROGRESS("진행 중"),
    END("챌린지 종료"),
    SUCCESS("챌린지 성공"),
    FAILED("챌린지 실패");

    private final String description;
}
