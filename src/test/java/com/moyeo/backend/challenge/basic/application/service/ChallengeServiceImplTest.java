package com.moyeo.backend.challenge.basic.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.application.dto.*;
import com.moyeo.backend.challenge.basic.application.mapper.ChallengeMapper;
import com.moyeo.backend.challenge.basic.application.mapper.ChallengeMapperImpl;
import com.moyeo.backend.challenge.basic.application.validator.ChallengeValidator;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.ChallengeOption;
import com.moyeo.backend.challenge.basic.domain.StartEndOption;
import com.moyeo.backend.challenge.basic.domain.TimeOption;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeStatus;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeType;
import com.moyeo.backend.challenge.basic.domain.repository.ChallengeInfoRepository;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.common.mapper.PageMapper;
import com.moyeo.backend.common.mapper.PageMapperImpl;
import com.moyeo.backend.common.request.PageRequestDto;
import com.moyeo.backend.common.response.PageResponse;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.domain.PaymentRepository;
import com.moyeo.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Mock
    private ChallengeValidator challengeValidator;

    @Spy
    private ChallengeMapper challengeMapper = new ChallengeMapperImpl();

    @Spy
    private PageMapper pageMapper = new PageMapperImpl();

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

    private Challenge createChallenge(String id, ChallengeType type, ChallengeOption option) {
        return Challenge.builder()
                .id(id)
                .type(type)
                .option(option)
                .build();
    }

    private ChallengeCreateRequestDto settingCreateReqDto(ChallengeType type, ChallengeOptionDto option) {
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

    private ChallengeReadRequestDto settingReadReqDto(String title, ChallengeType type, ChallengeStatus status) {
        return ChallengeReadRequestDto.builder()
                .title(title)
                .type(type)
                .status(status)
                .build();
    }

    private ChallengeReadResponseDto settingReadResDto(String title, ChallengeType type, ChallengeStatus status) {
        return ChallengeReadResponseDto.builder()
                .challengeId("CHALLENGE-UUID-1")
                .title(title)
                .type(type)
                .status(status)
                .build();
    }

    static Stream<Arguments> challengeTypeAndOptionDto() {
        return Stream.of(
                Arguments.of(
                        ChallengeType.TIME,
                        TimeOptionDto.builder()
                                .time(1440)
                                .build()
                ),
                Arguments.of(
                        ChallengeType.ATTENDANCE,
                        StartEndOptionDto.builder()
                                .start(LocalTime.parse("11:00"))
                                .end(LocalTime.parse("13:00"))
                                .build()
                ),
                Arguments.of(
                        ChallengeType.CONTENT,
                        StartEndOptionDto.builder()
                                .start(LocalTime.parse("11:00"))
                                .end(LocalTime.parse("13:00"))
                                .build()
                )
        );
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
                                .start(LocalTime.parse("11:00"))
                                .end(LocalTime.parse("13:00"))
                                .build()
                ),
                Arguments.of(
                        ChallengeType.CONTENT,
                        StartEndOption.builder()
                                .start(LocalTime.parse("11:00"))
                                .end(LocalTime.parse("13:00"))
                                .build()
                )
        );
    }

    @ParameterizedTest(name = "챌린지 생성 성공 테스트 {0}, {1}")
    @MethodSource("challengeTypeAndOptionDto")
    void challenge_생성_성공_테스트(ChallengeType type, ChallengeOptionDto option) {
        // given
        String paymentId = "PAYMENT-UUID-1";
        ChallengeCreateRequestDto requestDto = settingCreateReqDto(type, option);

        when(userContextService.getCurrentUser()).thenReturn(user);
        when(paymentRepository.findByIdAndIsDeletedFalse(paymentId)).thenReturn(Optional.of(payment));

        // when
        ChallengeResponseDto responseDto = challengeService.create(requestDto);

        // then
        verify(challengeInfoRepository).save(any(Challenge.class));
        verify(payment).updateParticipation(any(ChallengeParticipation.class));
        assertNotNull(responseDto);
    }

    @ParameterizedTest(name = "챌린지 생성 실패 테스트 - 결제 정보 없을 때 {0}, {1}")
    @MethodSource("challengeTypeAndOptionDto")
    void challenge_생성_실패_테스트_결제_정보_없을_때(ChallengeType type, ChallengeOptionDto option) {
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

    @ParameterizedTest(name = "챌린지 조회 성공 테스트 {0}, {1}")
    @MethodSource("challengeTypeAndOption")
    void challenge_단건_조회_성공_테스트(ChallengeType type, ChallengeOption option) {
        // given
        String challengeId = "CHALLENGE-UUID-1";
        Challenge challenge = createChallenge(challengeId, type, option);

        when(challengeInfoRepository.findByIdAndIsDeletedFalse(challengeId)).thenReturn(Optional.of(challenge));

        // when
        ChallengeReadResponseDto responseDto = challengeService.getById(challengeId);

        // then
        assertEquals(challengeId, responseDto.getChallengeId());
        assertEquals(type, responseDto.getType());
    }

    @Test
    @DisplayName("챌린지 단건 조회 실패 테스트 - 챌린지 정보 없음")
    void challenge_단건_조회_실패_테스트() {
        // given
        String challengeId = "CHALLENGE-UUID-1";

        when(challengeValidator.getValidChallengeById(challengeId)).thenThrow(
                new CustomException(ErrorCode.CHALLENGE_NOT_FOUND)
        );

        // when & then
        CustomException ex = assertThrows(CustomException.class, () -> {
            challengeService.getById(challengeId);
        });
        assertEquals(ErrorCode.CHALLENGE_NOT_FOUND, ex.getResponseCode());
    }

    @Test
    @DisplayName("챌린지 목록 조회 테스트")
    void challenge_목록_조회_테스트() {
        // given
        ChallengeReadRequestDto requestDto = settingReadReqDto(null, null, null);
        Pageable pageable = PageRequestDto.builder()
                .page(1)
                .size(10)
                .sort("createdAt")
                .direction("desc")
                .build().toPageable();
        ChallengeReadResponseDto responseDto = settingReadResDto("title", ChallengeType.TIME, ChallengeStatus.RECRUITING);
        Page<ChallengeReadResponseDto> results = new PageImpl<>(List.of());

        PageResponse<ChallengeReadResponseDto> expected = PageResponse.<ChallengeReadResponseDto>builder()
                .content(List.of(responseDto))
                .pageNumber(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalPages(1)
                .totalElements(1)
                .last(true)
                .build();

        when(challengeInfoRepository.searchChallenges(requestDto, pageable)).thenReturn(results);
        when(pageMapper.toPageResponse(results)).thenReturn(expected);

        // when
        PageResponse<ChallengeReadResponseDto> response = challengeService.gets(requestDto, pageable);

        // then
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getTitle()).isEqualTo("title");
        assertThat(response.getPageNumber()).isEqualTo(1);
        assertThat(response.getPageSize()).isEqualTo(10);
        assertThat(response.isLast()).isTrue();
    }
}