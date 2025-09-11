package com.moyeo.backend.challenge.basic.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeResponseDto;
import com.moyeo.backend.challenge.basic.application.mapper.ChallengeMapper;
import com.moyeo.backend.challenge.basic.application.validator.ChallengeValidator;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.repository.ChallengeInfoRepository;
import com.moyeo.backend.challenge.participation.application.mapper.ChallengeParticipationMapper;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipationRepository;
import com.moyeo.backend.challenge.participation.infrastructure.redis.ChallengeRedisKeyUtil;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.common.mapper.PageMapper;
import com.moyeo.backend.common.response.PageResponse;
import com.moyeo.backend.payment.application.validator.PaymentValidator;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j(topic = "ChallengeService")
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeMapper challengeMapper;
    private final ChallengeInfoRepository challengeInfoRepository;
    private final ChallengeParticipationRepository participationRepository;
    private final UserContextService userContextService;
    private final PageMapper pageMapper;
    private final ChallengeParticipationMapper participationMapper;
    private final StringRedisTemplate redisTemplate;
    private final ChallengeValidator challengeValidator;
    private final PaymentValidator paymentValidator;
    private final Clock clock;

    private static final Duration SLOTS_TTL = Duration.ofMinutes(5);

    @Override
    @Transactional
    public ChallengeResponseDto create(ChallengeCreateRequestDto requestDto) {

        User currentUser = userContextService.getCurrentUser();
        PaymentHistory payment = paymentValidator.getValidPaymentByIdAndUserId(requestDto.getPaymentId(), currentUser.getId());
        challengeValidator.validateChallengeStartDate(requestDto.getStartDate());

        Challenge challenge = challengeMapper.toChallenge(requestDto, currentUser);
        challengeInfoRepository.save(challenge);
        log.info("option : {}", challenge.getOption());

        ChallengeParticipation participation = participationMapper.toParticipant(challenge, currentUser);
        participationRepository.save(participation);
        payment.updateParticipation(participation);

        String challengeId = challenge.getId();
        try {
            redisTemplate.opsForValue().set(
                    ChallengeRedisKeyUtil.buildSlotsKey(challengeId),
                    String.valueOf(requestDto.getMaxParticipants() - 1),
                    Duration.between(
                            LocalDateTime.now(clock),
                            requestDto.getStartDate().atTime(23,59,59)
                    ).plusMinutes(5)
            );
        } catch (Exception e) {
            log.error("[Redis] Redis Slots 설정 실패 : {}, error = {}", challengeId, e.getMessage());
            throw new CustomException(ErrorCode.RIDES_SET_FAILED);
        }
        return ChallengeResponseDto.builder()
                .challengeId(challengeId)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ChallengeReadResponseDto getById(String challengeId) {
        Challenge challenge = challengeValidator.getValidChallengeById(challengeId);

        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();

        boolean participating = participationRepository.existsByChallengeIdAndUserIdAndIsDeletedFalse(challengeId, userId);
        return challengeMapper.toChallengeDto(challenge, participating);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ChallengeReadResponseDto> gets(ChallengeReadRequestDto requestDto, Pageable pageable) {

        Page<ChallengeReadResponseDto> challenges = challengeInfoRepository.searchChallenges(requestDto, pageable);
        return pageMapper.toPageResponse(challenges);
    }

    @Override
    @Transactional
    public void updateStatus(LocalDate date) {
        challengeInfoRepository.updateStatus(date);
        log.info("챌린지 상태 업데이트 완료, date = {}", date);
    }
}
