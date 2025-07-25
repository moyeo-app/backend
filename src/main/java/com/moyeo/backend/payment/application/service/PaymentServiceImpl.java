package com.moyeo.backend.payment.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.payment.application.dto.PaymentRequestDto;
import com.moyeo.backend.payment.application.dto.PaymentResponseDto;
import com.moyeo.backend.payment.application.mapper.PaymentMapper;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.domain.PaymentRepository;
import com.moyeo.backend.payment.domain.PaymentStatus;
import com.moyeo.backend.payment.domain.PaymentType;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j(topic = "PaymentServiceImpl")
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final UserContextService userContextService;

    @Override
    @Transactional
    public PaymentResponseDto create(PaymentRequestDto requestDto) {

        // user 정보 가져오기
        User currentUser = userContextService.getCurrentUser();

        validUser(currentUser.getId(), requestDto.getOrderId());
        validPayment(requestDto.getPaymentKey());

        PaymentHistory paymentHistory = paymentMapper.toPayment(
                requestDto,
                currentUser,
                PaymentType.PAYMENT,
                PaymentStatus.SUCCESS
        );

        paymentRepository.save(paymentHistory);

        return PaymentResponseDto.builder()
                .paymentId(paymentHistory.getId())
                .build();
    }

    // 소유권 체크 - orderId 비교
    private void validUser(String userId, String orderId) {
        if (!orderId.contains(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    // 이미 생성된 결제인지 확인
    private void validPayment(String paymentKey) {
        Optional<PaymentHistory> payment = paymentRepository.findByPaymentKeyAndIsDeletedFalse(paymentKey);
        if (payment.isPresent()) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_EXISTS);
        }
    }
}
