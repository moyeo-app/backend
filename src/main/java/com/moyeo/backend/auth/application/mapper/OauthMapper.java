package com.moyeo.backend.auth.application.mapper;

import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.user.application.dto.RegisterRequestDto;
import com.moyeo.backend.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OauthMapper {

    @Mapping(target = "user" , expression = "java(user)")
    Oauth toOauth(RegisterRequestDto requestDto, User user);
}
