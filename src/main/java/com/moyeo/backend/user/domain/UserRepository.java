package com.moyeo.backend.user.domain;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByNicknameAndIsDeletedFalse(String nickname);

    Optional<User> findByAccountNumberAndIsDeletedFalse(String accountNumber);

    Optional<User> findByIdAndIsDeletedFalse(String userId);
}
