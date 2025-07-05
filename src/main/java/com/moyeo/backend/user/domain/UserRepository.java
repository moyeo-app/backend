package com.moyeo.backend.user.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByNicknameAndIsDeletedFalse(String nickname);

    User save(User user);

    Optional<User> findByAccountNumberAndIsDeletedFalse(String accountNumber);
}
