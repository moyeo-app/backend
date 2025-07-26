package com.moyeo.backend.challenge.participation.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeStatus;
import com.moyeo.backend.challenge.basic.infrastructure.repository.JpaChallengeInfoRepository;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationResponseDto;
import com.moyeo.backend.challenge.participation.application.mapper.ChallengeParticipationMapper;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.challenge.participation.infrastructure.repository.JpaChallengeParticipationRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.infrastructure.JpaPaymentRepository;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j(topic = "ChallengeParticipationService")
@Service
@RequiredArgsConstructor
public class ChallengeParticipationServiceImpl implements ChallengeParticipationService {

    private final UserContextService userContextService;
    private final ChallengeParticipationMapper participationMapper;
    private final JpaChallengeParticipationRepository participationRepository;
    private final JpaChallengeInfoRepository challengeInfoRepository;
    private final JpaPaymentRepository paymentRepository;

    @Override
    @Transactional
    public ChallengeParticipationResponseDto participate(String challengeId, ChallengeParticipationRequestDto requestDto) {

        User currentUser = userContextService.getCurrentUser();
        Challenge challenge = validChallengeAndParticipation(challengeId, currentUser.getId());
        PaymentHistory payment = validPayment(requestDto.getPaymentId(), currentUser.getId());

        ChallengeParticipation participant = participationMapper.toParticipant(challenge, currentUser);
        payment.updateChallenge(participant);
        challenge.updateParticipantsCount();

        participationRepository.save(participant);

        return ChallengeParticipationResponseDto.builder()
                .participationId(participant.getId())
                .build();
    }

    private Challenge validChallengeAndParticipation(String challengeId, String userId) {
        Challenge challenge = challengeInfoRepository.findByIdAndIsDeletedFalse(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        Optional<ChallengeParticipation> participation = participationRepository.findByChallengeIdAndUserIdAndIsDeletedFalse(challengeId, userId);
        if (participation.isPresent()) {
            throw new CustomException(ErrorCode.PARTICIPATION_ALREADY_EXISTS);
        }

        if (challenge.getStatus() == ChallengeStatus.CLOSED) {
            throw new CustomException(ErrorCode.CHALLENGE_PARTICIPATION_CLOSED);
        }

        return challenge;
    }

    private PaymentHistory validPayment(String paymentId, String userId) {
        PaymentHistory payment = paymentRepository.findByIdAndIsDeletedFalse(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
        if (!payment.getOrderId().contains(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return payment;
    }
}
