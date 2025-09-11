package com.moyeo.backend.auth.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Provider {
    KAKAO("카카오"),
    GOOGLE("구글");

    private final String description;
}
