package com.moyeo.backend.user.application.validator;

import com.moyeo.backend.auth.domain.Oauth;
import com.moyeo.backend.auth.infrastructure.JpaOauthRepository;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.user.domain.User;
import com.moyeo.backend.user.infrastructure.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final JpaUserRepository userRepository;
    private final JpaOauthRepository oauthRepository;

    // 닉네임 유효성 검사
    public void validateNickname(String nickname) {
        if (userRepository.findByNicknameAndIsDeletedFalse(nickname).isPresent()) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    // 이미 가입된 계정인지 확인
    public void validateOauthIdAndAccount(String oauthId, String accountNumber) {
        Optional<Oauth> oauth = oauthRepository.findByOauthIdAndIsDeletedFalse(oauthId);
        Optional<User> user = userRepository.findByAccountNumberAndIsDeletedFalse(accountNumber);

        if (oauth.isPresent()) {
            throw new CustomException(ErrorCode.OAUTH_ALREADY_EXISTS);
        } else if (user.isPresent()) {
            throw new CustomException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
    }

    public User getValidUserById(String userId) {
        return userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
