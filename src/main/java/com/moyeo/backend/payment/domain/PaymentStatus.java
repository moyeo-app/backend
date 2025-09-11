package com.moyeo.backend.payment.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentStatus {
    PENDING("결제 처리중"),
    SUCCESS("결제 성공"),
    FAILED("결제 실패"),
    CANCELED("결제 취소");

    private final String description;
}