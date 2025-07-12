package com.moyeo.backend.challenge.basic.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeResponseDto;
import com.moyeo.backend.challenge.basic.application.mapper.ChallengeMapper;
import com.moyeo.backend.challenge.basic.application.mapper.ChallengeMapperImpl;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.ChallengeOption;
import com.moyeo.backend.challenge.basic.domain.StartEndOption;
import com.moyeo.backend.challenge.basic.domain.TimeOption;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeType;
import com.moyeo.backend.challenge.basic.domain.repository.ChallengeInfoRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.domain.PaymentRepository;
import com.moyeo.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceImplTest {

    @InjectMocks
    private ChallengeServiceImpl challengeService;

    @Mock
    private UserContextService userContextService;

    @Mock
    private ChallengeInfoRepository challengeInfoRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentHistory payment;

    @Spy
    private ChallengeMapper challengeMapper = new ChallengeMapperImpl();

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

    private ChallengeCreateRequestDto settingCreateReqDto(ChallengeType type, ChallengeOption option) {
        return ChallengeCreateRequestDto.builder()
                .title("title")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .type(type)
                .maxParticipants(20)
                .fee(5000)
                .description("description")
                .option(option)
                .rule(3)
                .paymentId("PAYMENT-UUID-1")
                .build();
    }

    static Stream<Arguments> challengeTypeAndOption() {
        return Stream.of(
                Arguments.of(
                        ChallengeType.TIME,
                        TimeOption.builder()
                                .time(1440)
                                .build()
                ),
                Arguments.of(
                        ChallengeType.ATTENDANCE,
                        StartEndOption.builder()
                                .start("11:00")
                                .end("13:00")
                                .build()
                ),
                Arguments.of(
                        ChallengeType.CONTENT,
                        StartEndOption.builder()
                                .start("11:00")
                                .end("13:00")
                                .build()
                )
        );
    }

    @ParameterizedTest(name = "챌린지 생성 성공 테스트 {0}, {1}")
    @MethodSource("challengeTypeAndOption")
    void challenge_생성_성공_테스트(ChallengeType type, ChallengeOption option) {
        // given
        String paymentId = "PAYMENT-UUID-1";
        ChallengeCreateRequestDto requestDto = settingCreateReqDto(type, option);

        when(userContextService.getCurrentUser()).thenReturn(user);
        when(paymentRepository.findByIdAndIsDeletedFalse(paymentId)).thenReturn(Optional.of(payment));

        // when
        ChallengeResponseDto responseDto = challengeService.create(requestDto);

        // then
        verify(challengeInfoRepository).save(any(Challenge.class));
        verify(payment).updateChallenge(any(Challenge.class));
        assertNotNull(responseDto);
    }

    @ParameterizedTest(name = "챌린지 생성 실패 테스트 - 결제 정보 없을 때 {0}, {1}")
    @MethodSource("challengeTypeAndOption")
    void challenge_생성_실패_테스트_결제_정보_없을_때(ChallengeType type, ChallengeOption option) {
        // given
        String paymentId = "PAYMENT-UUID-1";
        ChallengeCreateRequestDto requestDto = settingCreateReqDto(type, option);

        when(userContextService.getCurrentUser()).thenReturn(user);
        when(paymentRepository.findByIdAndIsDeletedFalse(paymentId)).thenReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class, () -> {
            challengeService.create(requestDto);
        });
        assertEquals(ErrorCode.PAYMENT_NOT_FOUND, ex.getResponseCode());
    }
}