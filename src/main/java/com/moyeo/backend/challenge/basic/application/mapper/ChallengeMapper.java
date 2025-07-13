package com.moyeo.backend.challenge.basic.application.mapper;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeOptionDto;
import com.moyeo.backend.challenge.basic.application.dto.StartEndOptionDto;
import com.moyeo.backend.challenge.basic.application.dto.TimeOptionDto;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.ChallengeOption;
import com.moyeo.backend.challenge.basic.domain.StartEndOption;
import com.moyeo.backend.challenge.basic.domain.TimeOption;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
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
    @Mapping(target = "option", expression = "java(toOption(requestDto.getOption()))")
    Challenge toChallenge(ChallengeCreateRequestDto requestDto, User user);

    default ChallengeOption toOption(ChallengeOptionDto dto) {
        if (dto instanceof TimeOptionDto time) {
            return TimeOption.builder()
                    .time(time.getTime())
                    .build();
        } else if (dto instanceof StartEndOptionDto startEnd) {
            return StartEndOption.builder()
                    .start(startEnd.getStart())
                    .end(startEnd.getEnd())
                    .build();
        } else {
            throw new CustomException(ErrorCode.INVALID_OPTION_FORMAT);
        }
    }
}
