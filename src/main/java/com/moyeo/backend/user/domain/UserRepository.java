package com.moyeo.backend.user.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByNickname(String nickname);

    User save(User user);
}
