package com.moyeo.backend.payment.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.payment.application.dto.PaymentRequestDto;
import com.moyeo.backend.payment.application.dto.PaymentResponseDto;
import com.moyeo.backend.payment.application.mapper.PaymentMapper;
import com.moyeo.backend.payment.application.mapper.PaymentMapperImpl;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.domain.PaymentMethod;
import com.moyeo.backend.payment.domain.PaymentRepository;
import com.moyeo.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private UserContextService userContextService;

    @Mock
    private PaymentRepository paymentRepository;

    @Spy
    private PaymentMapper paymentMapper = new PaymentMapperImpl();

    private User user;

    @BeforeEach
    void setupSecurityContext() {
        user = settingUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private User settingUser() {
        return User.builder()
                .id("USER-UUID-1")
                .build();
    }

    private PaymentRequestDto settingPayment(String paymentKey, String orderId) {
        return PaymentRequestDto.builder()
                .method(PaymentMethod.계좌이체)
                .paymentKey(paymentKey)
                .totalAmount(5000)
                .orderId(orderId)
                .build();
    }

    @Test
    @DisplayName("결제 생성 성공 테스트")
    void payment_생성_성공_테스트() {
        // given
        String paymentKey = "paymentKey";
        String orderId = "CHALLENGE-UUID-1_USER-UUID-1";
        PaymentRequestDto requestDto = settingPayment(paymentKey, orderId);

        when(userContextService.getCurrentUser()).thenReturn(user);
        when(paymentRepository.findByPaymentKeyAndIsDeletedFalse(paymentKey)).thenReturn(Optional.empty());

        // when
        PaymentResponseDto responseDto = paymentService.create(requestDto);

        // then
        verify(paymentRepository).save(any(PaymentHistory.class));
        assertNotNull(responseDto);
    }

    @Test
    @DisplayName("결제 생성 실패 테스트 - 중복 결제키")
    void payment_생성_실패_테스트_중복_결제키() {
        // given
        String paymentKey = "duplicatePaymentKey";
        String orderId = "CHALLENGE-UUID-1_USER-UUID-1";
        PaymentRequestDto requestDto = settingPayment(paymentKey, orderId);

        when(userContextService.getCurrentUser()).thenReturn(user);
        when(paymentRepository.findByPaymentKeyAndIsDeletedFalse(paymentKey))
                .thenReturn(Optional.of(mock(PaymentHistory.class)));

        // when & then
        CustomException ex = assertThrows(CustomException.class, () -> {
            paymentService.create(requestDto);
        });
        assertEquals(ErrorCode.PAYMENT_ALREADY_EXISTS, ex.getResponseCode());
    }

    @Test
    @DisplayName("결제 생성 실패 테스트 - 소유권 불일치")
    void payment_생성_실패_테스트_소유권_불일치() {
        // given
        String paymentKey = "paymentKey";
        String orderId = "CHALLENGE-UUID-1_USER-UUID-2";
        PaymentRequestDto requestDto = settingPayment(paymentKey, orderId);

        when(userContextService.getCurrentUser()).thenReturn(user);

        // when & then
        CustomException ex = assertThrows(CustomException.class, () -> {
            paymentService.create(requestDto);
        });
        assertEquals(ErrorCode.UNAUTHORIZED, ex.getResponseCode());
    }
}