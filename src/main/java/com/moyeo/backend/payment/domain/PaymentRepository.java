package com.moyeo.backend.payment.domain;

import java.util.Optional;

public interface PaymentRepository {
    PaymentHistory save(PaymentHistory payment);

    Optional<PaymentHistory> findByPaymentKeyAndIsDeletedFalse(String paymentKey);
}