package com.moyeo.backend.payment.domain;

import java.util.Optional;

public interface PaymentRepository {
    Optional<PaymentHistory> findByPaymentKeyAndIsDeletedFalse(String paymentKey);

    Optional<PaymentHistory> findByIdAndIsDeletedFalse(String paymentId);

    Optional<PaymentHistory> findByIdAndUserIdAndIsDeletedFalse(String paymentId, String userId);
}