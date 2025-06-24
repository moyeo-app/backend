package com.moyeo.backend.user.application.service;

import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.domain.OauthRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
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

    @Override
    public UserResponseDto register(RegisterRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        validNickname(nickname);

        User user = User.builder()
                .nickname(nickname)
                .character(requestDto.getCharacter())
                .build();

        Oauth oauth = Oauth.builder()
                .oauthId(requestDto.getOauthId())
                .user(user)
                .provider(requestDto.getProvider())
                .build();

        userRepository.save(user);
        oauthRepository.save(oauth);
        return UserResponseDto.builder().userId(user.getId()).build();
    }

    public void validNickname(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);

        if (user.isPresent()) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }
}
