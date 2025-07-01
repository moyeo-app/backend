package com.moyeo.backend.user.application.service;

import com.moyeo.backend.auth.application.mapper.OauthMapper;
import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.user.application.mapper.UserMapper;
import com.moyeo.backend.user.domain.User;
import com.moyeo.backend.user.domain.UserRepository;
import com.moyeo.backend.user.application.dto.RegisterRequestDto;
import com.moyeo.backend.user.application.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OauthRepository oauthRepository;
    private final UserMapper userMapper;
    private final OauthMapper oauthMapper;

    @Override
    public UserResponseDto register(RegisterRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        validNickname(nickname);

        User user = userMapper.toUser(requestDto);
        Oauth oauth = oauthMapper.toOauth(requestDto, user);

        userRepository.save(user);
        oauthRepository.save(oauth);
        return UserResponseDto.builder().userId(user.getId()).build();
    }

    public void validNickname(String nickname) {
        Optional<User> user = userRepository.findByNicknameAndIsDeletedFalse(nickname);

        if (user.isPresent()) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }
}
