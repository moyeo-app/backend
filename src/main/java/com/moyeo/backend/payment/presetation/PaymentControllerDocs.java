package com.moyeo.backend.payment.presetation;

import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.payment.application.dto.PaymentRequestDto;
import com.moyeo.backend.payment.application.dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "결제 관련 API Controller", description = "결제 관련 API 목록입니다.")
public interface PaymentControllerDocs {

    @Operation(summary = "결제 생성 API", description = "결제 생성 API 입니다.")
    ResponseEntity<ApiResponse<PaymentResponseDto>> create(
            PaymentRequestDto requestDto);

}