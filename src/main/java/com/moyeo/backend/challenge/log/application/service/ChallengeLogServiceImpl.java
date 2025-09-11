package com.moyeo.backend.challenge.log.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.application.validator.ChallengeValidator;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.log.application.dto.*;
import com.moyeo.backend.challenge.log.application.mapper.ChallengeLogMapper;
import com.moyeo.backend.challenge.log.application.validator.ChallengeLogValidator;
import com.moyeo.backend.challenge.log.domain.ChallengeLog;
import com.moyeo.backend.challenge.log.domain.ChallengeLogRepository;
import com.moyeo.backend.challenge.log.domain.ChallengeLogStatus;
import com.moyeo.backend.challenge.log.domain.ContentLog;
import com.moyeo.backend.challenge.participation.application.validator.ChallengeParticipationValidator;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.common.mapper.PageMapper;
import com.moyeo.backend.common.response.PageResponse;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "ChallengeLogService")
@Service
@RequiredArgsConstructor
public class ChallengeLogServiceImpl implements ChallengeLogService {

    private final UserContextService userContextService;
    private final ChallengeValidator challengeValidator;
    private final ChallengeParticipationValidator participationValidator;
    private final ChallengeLogValidator logValidator;
    private final ChallengeLogRepository challengeLogRepository;
    private final ChallengeLogMapper challengeLogMapper;
    private final PageMapper pageMapper;

    @Override
    @Transactional
    public ChallengeLogResponseDto create(String challengeId, ChallengeLogKeywordRequestDto requestDto) {
        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();

        Challenge challenge = challengeValidator.getValidContentChallengeByIdAndInTime(challengeId);
        ChallengeParticipation participation = participationValidator.getValidParticipationByUserId(challengeId, userId);

        ContentLog content = challengeLogMapper.toContentLog(requestDto);
        ChallengeLog challengeLog = challengeLogMapper.toLog(challenge, participation, ChallengeLogStatus.PENDING, content);

        challengeLogRepository.save(challengeLog);

        return ChallengeLogResponseDto.builder()
                .logId(challengeLog.getId())
                .build();
    }

    @Override
    @Transactional
    public ChallengeLogResponseDto update(String challengeId, String logId, ChallengeLogContentRequestDto requestDto) {
        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();

        challengeValidator.getValidDateAndContentChallengeById(challengeId);
        ChallengeParticipation participation = participationValidator.getValidParticipationByUserId(challengeId, userId);
        ChallengeLog challengeLog = logValidator.getValidLogById(logId);
        logValidator.validLogOwnership(participation.getId(), challengeLog.getParticipation().getId());

        String text = requestDto.getText();
        logValidator.validText(logId, text);
        challengeLog.updateContent(text);

        return ChallengeLogResponseDto.builder()
                .logId(challengeLog.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ChallengeLogReadResponseDto> gets(String challengeId, ChallengeLogReadRequestDto requestDto, Pageable pageable) {
        Page<ChallengeLogReadResponseDto> logs = challengeLogRepository.getLogs(challengeId, requestDto, pageable);
        return pageMapper.toPageResponse(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public ChallengeLogReadResponseDto getByUser(String challengeId) {
        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();

        Challenge challenge = challengeValidator.getValidChallengeById(challengeId);
        ChallengeParticipation participation = participationValidator.getValidParticipationByUserId(challenge.getId(), userId);
        ChallengeLog challengeLog = logValidator.getValidLogByUser(participation.getId());
        return challengeLogMapper.toLogDto(challengeLog);
    }
}
