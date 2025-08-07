package com.moyeo.backend.payment.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.payment.application.dto.PaymentRequestDto;
import com.moyeo.backend.payment.application.dto.PaymentResponseDto;
import com.moyeo.backend.payment.application.mapper.PaymentMapper;
import com.moyeo.backend.payment.application.validator.PaymentValidator;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.domain.PaymentRepository;
import com.moyeo.backend.payment.domain.PaymentStatus;
import com.moyeo.backend.payment.domain.PaymentType;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "PaymentServiceImpl")
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final UserContextService userContextService;
    private final PaymentValidator paymentValidator;

    @Override
    @Transactional
    public PaymentResponseDto create(PaymentRequestDto requestDto) {

        // user 정보 가져오기
        User currentUser = userContextService.getCurrentUser();

        paymentValidator.validateOrderOwnership(currentUser.getId(), requestDto.getOrderId());
        paymentValidator.validateNotExistsByPaymentKey(requestDto.getPaymentKey());

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
}
