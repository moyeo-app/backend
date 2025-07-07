package com.moyeo.backend.payment.presetation;

import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.payment.application.dto.PaymentRequestDto;
import com.moyeo.backend.payment.application.dto.PaymentResponseDto;
import com.moyeo.backend.payment.application.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "PaymentController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController implements PaymentControllerDocs {

    private final PaymentService paymentService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponseDto>> create(@Valid @RequestBody PaymentRequestDto requestDto) {
        log.info("Payment Key : {}", requestDto.getPaymentKey());
        return ResponseEntity.ok().body(ApiResponse.success(paymentService.create(requestDto)));
    }
}
