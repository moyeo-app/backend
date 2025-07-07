package com.moyeo.backend.payment.application.service;

import com.moyeo.backend.payment.application.dto.PaymentRequestDto;
import com.moyeo.backend.payment.application.dto.PaymentResponseDto;

public interface PaymentService {

    PaymentResponseDto create(PaymentRequestDto requestDto);
}
