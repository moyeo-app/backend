package com.moyeo.backend.payment.application.validator;

import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.infrastructure.JpaPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentValidator {

    private final JpaPaymentRepository paymentRepository;

    // 소유권 확인 - orderId 비교
    public void validateOrderOwnership(String userId, String orderId) {
        if (!orderId.contains(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    // 이미 생성된 결제인지 확인
    public void validateNotExistsByPaymentKey(String paymentKey) {
        Optional<PaymentHistory> payment = paymentRepository.findByPaymentKeyAndIsDeletedFalse(paymentKey);
        if (payment.isPresent()) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_EXISTS);
        }
    }

    // 결제 정보 및 소유권 확인
    public PaymentHistory getValidPaymentByIdAndUserId(String paymentId, String userId) {
        return paymentRepository.findByIdAndUserIdAndIsDeletedFalse(paymentId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

}
