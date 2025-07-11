package com.moyeo.backend.challenge.basic.application.mapper;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ChallengeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(user)")
    @Mapping(target = "participantsCount", constant = "1")
    @Mapping(target = "status", constant = "RECRUITING")
    Challenge toChallenge(ChallengeCreateRequestDto requestDto, User user);
}
