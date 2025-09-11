package com.moyeo.backend.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "결제 확인 RESPONSE DTO")
public class PaymentResponseDto {

    @Schema(description = "생성된 결제 ID", example = "ed8f52fe-f9c0-41db-9e52-57f25185c382")
    private String paymentId;
}
