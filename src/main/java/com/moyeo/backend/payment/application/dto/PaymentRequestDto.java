package com.moyeo.backend.payment.application.dto;

import com.moyeo.backend.payment.domain.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "결제 생성 REQUEST DTO")
public class PaymentRequestDto {

    @Schema(description = "결제 수단", example = "계좌이체")
    @NotNull
    private PaymentMethod method;

    @Schema(description = "PG 사에서 받은 결제 키", example = "pmt_20250529_xxxxx")
    @NotBlank
    private String paymentKey;

    @Schema(description = "결제 금액", example = "10000")
    @NotNull
    @Min(5000)
    @Max(50000)
    private Integer totalAmount;

    @Schema(description = "주문 ID", example = "challenge_3_user_17")
    @NotBlank
    private String orderId;
}
