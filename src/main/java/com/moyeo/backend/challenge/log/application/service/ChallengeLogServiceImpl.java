package com.moyeo.backend.challenge.log.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.challenge.basic.application.validator.ChallengeValidator;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogContentRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogKeywordRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogResponseDto;
import com.moyeo.backend.challenge.log.application.mapper.ChallengeLogMapper;
import com.moyeo.backend.challenge.log.application.validator.ChallengeLogValidator;
import com.moyeo.backend.challenge.log.domain.ChallengeLog;
import com.moyeo.backend.challenge.log.domain.ChallengeLogRepository;
import com.moyeo.backend.challenge.log.domain.ChallengeLogStatus;
import com.moyeo.backend.challenge.log.domain.ContentLog;
import com.moyeo.backend.challenge.participation.application.validator.ChallengeParticipationValidator;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        challengeValidator.getValidContentChallengeById(challengeId);
        participationValidator.getValidParticipationByUserId(challengeId, userId);

        String text = requestDto.getText();
        ChallengeLog challengeLog = logValidator.getValidText(logId, text);
        challengeLog.updateContent(text);

        return ChallengeLogResponseDto.builder()
                .logId(challengeLog.getId())
                .build();
    }
}
