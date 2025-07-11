package com.moyeo.backend.challenge.basic.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChallengeType {
    TIME("시간 인증제"),
    ATTENDANCE("출석 인증제"),
    CONTENT("내용 인증제");

    private final String description;
}
