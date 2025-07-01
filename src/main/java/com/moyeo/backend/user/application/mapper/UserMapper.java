package com.moyeo.backend.user.application.mapper;

import com.moyeo.backend.user.application.dto.RegisterRequestDto;
import com.moyeo.backend.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "role", constant = "USER")
    User toUser(RegisterRequestDto requestDto);
}
