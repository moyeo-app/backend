package com.moyeo.backend.user.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByNicknameAndIsDeletedFalse(String nickname);

    Optional<User> findByAccountNumberAndIsDeletedFalse(String accountNumber);

    Optional<User> findByIdAndIsDeletedFalse(String userId);
}
