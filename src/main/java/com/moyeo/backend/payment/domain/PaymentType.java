package com.moyeo.backend.payment.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentType {
    PAYMENT("결제"),
    REFUND("환불");

    private final String description;
}