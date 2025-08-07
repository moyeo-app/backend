package com.moyeo.backend.user.application.service;

import com.moyeo.backend.auth.application.mapper.OauthMapper;
import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import com.moyeo.backend.user.application.dto.RegisterRequestDto;
import com.moyeo.backend.user.application.dto.UserResponseDto;
import com.moyeo.backend.user.application.mapper.UserMapper;
import com.moyeo.backend.user.application.validator.UserValidator;
import com.moyeo.backend.user.domain.User;
import com.moyeo.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OauthRepository oauthRepository;
    private final UserMapper userMapper;
    private final OauthMapper oauthMapper;
    private final UserValidator userValidator;

    @Override
    @Transactional
    public UserResponseDto register(RegisterRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        userValidator.validateNickname(nickname);
        userValidator.validateOauthIdAndAccount(requestDto.getOauthId(), requestDto.getAccountNumber());

        User user = userMapper.toUser(requestDto);
        Oauth oauth = oauthMapper.toOauth(requestDto, user);

        userRepository.save(user);
        oauthRepository.save(oauth);
        return UserResponseDto.builder().userId(user.getId()).build();
    }

    @Override
    @Transactional
    public void validNickname(String nickname) {
        userValidator.validateNickname(nickname);
    }
}
