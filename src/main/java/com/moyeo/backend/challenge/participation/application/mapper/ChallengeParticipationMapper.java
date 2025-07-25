package com.moyeo.backend.challenge.participation.application.mapper;

import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ChallengeParticipationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "challenge", expression = "java(challenge)")
    @Mapping(target = "user", expression = "java(user)")
    @Mapping(target = "status", ignore = true)
    ChallengeParticipation toParticipant(Challenge challenge, User user);
}
