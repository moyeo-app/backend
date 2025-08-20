package com.moyeo.backend.challenge.log.application.mapper;

import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogKeywordRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogReadResponseDto;
import com.moyeo.backend.challenge.log.domain.ChallengeLog;
import com.moyeo.backend.challenge.log.domain.ChallengeLogContent;
import com.moyeo.backend.challenge.log.domain.ChallengeLogStatus;
import com.moyeo.backend.challenge.log.domain.ContentLog;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ChallengeLogMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "challenge", expression = "java(challenge)")
    @Mapping(target = "participation", expression = "java(participation)")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now(java.time.ZoneId.of(\"Asia/Seoul\")))")
    @Mapping(target = "status", source = "status")
    ChallengeLog toLog(Challenge challenge, ChallengeParticipation participation, ChallengeLogStatus status, ChallengeLogContent content);

    @Mapping(source = "id", target = "logId")
    @Mapping(source = "participation.user.nickname", target = "nickname")
    ChallengeLogReadResponseDto toLogDto(ChallengeLog challengeLog);

    default ContentLog toContentLog(ChallengeLogKeywordRequestDto dto) {
        List<String> keywords = List.of(dto.getKeyword1(), dto.getKeyword2(), dto.getKeyword3());

        return ContentLog.builder()
                .keywords(keywords)
                .build();
    }
}
