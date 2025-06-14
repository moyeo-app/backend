package com.moyeo.backend.auth.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    USER("일반 사용자"),
    ADMIN("관리자");

    private final String description;
}
