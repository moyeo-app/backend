package com.moyeo.backend.challenge.log.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChallengeLogStatus {
    PENDING("인증 대기"),
    SUCCESS("인증 성공"),
    FAILED("인증 실패");

    private final String description;
}
