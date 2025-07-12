package com.moyeo.backend.challenge.basic.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeResponseDto;
import com.moyeo.backend.challenge.basic.application.mapper.ChallengeMapper;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.repository.ChallengeInfoRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.domain.PaymentRepository;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "ChallengeService")
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeMapper challengeMapper;
    private final ChallengeInfoRepository challengeInfoRepository;
    private final PaymentRepository paymentRepository;
    private final UserContextService userContextService;

    @Override
    @Transactional
    public ChallengeResponseDto create(ChallengeCreateRequestDto requestDto) {

        // user 정보 가져오기
        User currentUser = userContextService.getCurrentUser();
        PaymentHistory payment = validPayment(requestDto.getPaymentId());

        Challenge challenge = challengeMapper.toChallenge(requestDto, currentUser);
        challengeInfoRepository.save(challenge);
        payment.updateChallenge(challenge);

        return ChallengeResponseDto.builder()
                .challengeId(challenge.getId())
                .build();
    }

    // 결제 정보 확인
    private PaymentHistory validPayment(String paymentId) {
         return paymentRepository.findByIdAndIsDeletedFalse(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }
}
