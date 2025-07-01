package com.moyeo.backend.user.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Bank {
    KB("국민은행"),
    SHINHAN("신한은행"),
    WOORI("우리은행"),
    NH("농협은행"),
    HANA("하나은행"),
    TOSS("토스뱅크"),
    KAKAO("카카오뱅크");

    private final String name;
}
