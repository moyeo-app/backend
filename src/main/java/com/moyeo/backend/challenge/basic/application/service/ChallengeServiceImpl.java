package com.moyeo.backend.challenge.basic.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeResponseDto;
import com.moyeo.backend.challenge.basic.application.mapper.ChallengeMapper;
import com.moyeo.backend.challenge.basic.application.validator.ChallengeValidator;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.infrastructure.repository.JpaChallengeInfoRepository;
import com.moyeo.backend.challenge.participation.application.mapper.ChallengeParticipationMapper;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.challenge.participation.infrastructure.repository.JpaChallengeParticipationRepository;
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

@Slf4j(topic = "ChallengeService")
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeMapper challengeMapper;
    private final JpaChallengeInfoRepository challengeInfoRepository;
    private final JpaChallengeParticipationRepository participationRepository;
    private final UserContextService userContextService;
    private final PageMapper pageMapper;
    private final ChallengeParticipationMapper participationMapper;
    private final StringRedisTemplate redisTemplate;
    private final ChallengeValidator challengeValidator;
    private final PaymentValidator paymentValidator;

    @Override
    @Transactional
    public ChallengeResponseDto create(ChallengeCreateRequestDto requestDto) {

        User currentUser = userContextService.getCurrentUser();
        PaymentHistory payment = paymentValidator.getValidPaymentByIdAndUserId(requestDto.getPaymentId(), currentUser.getId());
        challengeValidator.validateDate(requestDto.getStartDate());

        Challenge challenge = challengeMapper.toChallenge(requestDto, currentUser);
        challengeInfoRepository.save(challenge);
        log.info("option : {}", challenge.getOption());

        ChallengeParticipation participation = participationMapper.toParticipant(challenge, currentUser);
        participationRepository.save(participation);
        payment.updateChallenge(participation);

        String challengeId = challenge.getId();
        redisTemplate.opsForValue().set("challengeId:" + challengeId + ":slots", String.valueOf(requestDto.getMaxParticipants()));
        return ChallengeResponseDto.builder()
                .challengeId(challengeId)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ChallengeReadResponseDto getById(String id) {
        Challenge challenge = challengeValidator.getValidChallengeById(id);
        return challengeMapper.toChallengeDto(challenge);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ChallengeReadResponseDto> gets(ChallengeReadRequestDto requestDto, Pageable pageable) {

        Page<ChallengeReadResponseDto> challenges = challengeInfoRepository.searchChallenges(requestDto, pageable);
        return pageMapper.toPageResponse(challenges);
    }
}
